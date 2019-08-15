public class PicassoBitmapDownloader extends UrlConnectionDownloader {

    private static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

    @NonNull private Context context;
    @Nullable private DiskLruCache diskCache;

    public class IfModifiedResponse extends Response {

        private final String ifModifiedSinceDate;

        public IfModifiedResponse(InputStream stream, boolean loadedFromCache, long contentLength, String ifModifiedSinceDate) {

            super(stream, loadedFromCache, contentLength);
            this.ifModifiedSinceDate = ifModifiedSinceDate;
        }

        public String getIfModifiedSinceDate() {

            return ifModifiedSinceDate;
        }
    }

    public PicassoBitmapDownloader(@NonNull Context context) {

        super(context);
        this.context = context;
    }

    @Override
    public Response load(final Uri uri, int networkPolicy) throws IOException {

        final String key = getKey(uri);
        {
            Response cachedResponse = getCachedBitmap(key);
            if (cachedResponse != null) {
                return cachedResponse;
            }
        }

        IfModifiedResponse response = _load(uri);

        if (cacheBitmap(key, response.getInputStream(), response.getIfModifiedSinceDate())) {

            IfModifiedResponse cachedResponse = getCachedBitmap(key);
            if (cachedResponse != null) {return cachedResponse;
            }
        }

        return response;
    }

    @NonNull
    protected IfModifiedResponse _load(Uri uri) throws IOException {

        HttpURLConnection connection = openConnection(uri);

        int responseCode = connection.getResponseCode();
        if (responseCode >= 300) {
            connection.disconnect();
            throw new ResponseException(responseCode + " " + connection.getResponseMessage(),
                    0, responseCode);
        }

        long contentLength = connection.getHeaderFieldInt("Content-Length", -1);
        String lastModified = connection.getHeaderField(Constants.HEADER_LAST_MODIFIED);
        return new IfModifiedResponse(connection.getInputStream(), false, contentLength, lastModified);
    }

    @Override
    protected HttpURLConnection openConnection(Uri path) throws IOException {

        HttpURLConnection conn = super.openConnection(path);

        DiskLruCache diskCache = getDiskCache();
        DiskLruCache.Snapshot snapshot = diskCache == null ? null : diskCache.get(getKey(path));
        if (snapshot != null) {
            String ifModifiedSince = snapshot.getString(1);
            if (!isEmpty(ifModifiedSince)) {
                conn.addRequestProperty(Constants.HEADER_IF_MODIFIED_SINCE, ifModifiedSince);
            }
        }

        return conn;
    }

    @Override public void shutdown() {

        try {
            if (diskCache != null) {
                diskCache.flush();
                diskCache.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        super.shutdown();
    }

    public boolean cacheBitmap(@Nullable String key, @Nullable InputStream inputStream, @Nullable String ifModifiedSince) {

        if (inputStream == null || isEmpty(key)) {
            return false;
        }

        OutputStream outputStream = null;
        DiskLruCache.Editor edit = null;
        try {
            DiskLruCache diskCache = getDiskCache();
            edit = diskCache == null ? null : diskCache.edit(key);
            outputStream = edit == null ? null : new BufferedOutputStream(edit.newOutputStream(0));

            if (outputStream == null) {
                return false;
            }

            ChatUtils.copy(inputStream, outputStream);
            outputStream.flush();

            edit.set(1, ifModifiedSince == null ? "" : ifModifiedSince);
            edit.commit();

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {

            if (edit != null) {
                edit.abortUnlessCommitted();
            }

            ChatUtils.closeQuietly(outputStream);
        }
        return false;
    }

    @Nullable
    public IfModifiedResponse getCachedBitmap(String key) {

        try {
            DiskLruCache diskCache = getDiskCache();
            DiskLruCache.Snapshot snapshot = diskCache == null ? null : diskCache.get(key);
            InputStream inputStream = snapshot == null ? null : snapshot.getInputStream(0);

            if (inputStream == null) {
                return null;
            }

            return new IfModifiedResponse(inputStream, true, snapshot.getLength(0), snapshot.getString(1));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Nullable
    synchronized public DiskLruCache getDiskCache() {

        if (diskCache == null) {

            try {
                File file = new File(context.getCacheDir() + "/images");
                if (!file.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    file.mkdirs();
                }

                long maxSize = calculateDiskCacheSize(file);
                diskCache = DiskLruCache.open(file, BuildConfig.VERSION_CODE, 2, maxSize);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return diskCache;
    }

    @NonNull
    private String getKey(@NonNull Uri uri) {

        String key = md5(uri.toString());
        return isEmpty(key) ? String.valueOf(uri.hashCode()) : key;
    }

    @Nullable
    public static String md5(final String toEncrypt) {

        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(String.format("%02X", aByte));
            }
            return sb.toString().toLowerCase();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static long calculateDiskCacheSize(File dir) {

        long available = ChatUtils.bytesAvailable(dir);
        // Target 2% of the total space.
        long size = available / 50;
        // Bound inside min/max size for disk cache.
        return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE);
    }
}
