public class CacheManager {

Cache<String, String> mCache;
private DiskLruCache mDiskLruCache;
private final Context mContext;

public CacheManager(Context context) throws IOException {
    mContext = context;
    setUp();
    mCache = DiskCache.getInstanceUsingDoubleLocking(mDiskLruCache);
}

public void setUp() throws IOException {
    File cacheInFiles = mContext.getFilesDir();
    int version = BuildConfig.VERSION_CODE;

    int KB = 1024;
    int MB = 1024 * KB;
    int cacheSize = 400 * MB;

    mDiskLruCache = DiskLruCache.open(cacheInFiles, version, 1, cacheSize);
}

public Cache<String, String> getCache() {
    return mCache;
}

public static class DiskCache implements Cache<String, String> {

    private static DiskLruCache mDiskLruCache;
    private static DiskCache instance = null;

    public static DiskCache getInstanceUsingDoubleLocking(DiskLruCache diskLruCache){
        mDiskLruCache = diskLruCache;
        if(instance == null){
            synchronized (DiskCache.class) {
                if(instance == null){
                    instance = new DiskCache();
                }
            }
        }
        return instance;
    }

    @Override
    public synchronized void put(String key, String value) {
        try {
            if (mDiskLruCache != null) {
                DiskLruCache.Editor edit = mDiskLruCache.edit(getMd5Hash(key));
                if (edit != null) {
                    edit.set(0, value);
                    edit.commit();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized String get(String key) {
        try {
            if (mDiskLruCache != null) {
                DiskLruCache.Snapshot snapshot = mDiskLruCache.get(getMd5Hash(key));

                if (snapshot == null) {
                    // if there is a cache miss simply return null;
                    return null;
                }

                return snapshot.getString(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // in case of error in reading return null;
        return null;
    }

    @Override
    public String remove(String key) {
        // TODO: implement
        return null;
    }

    @Override
    public void clear() {
        // TODO: implement
    }
}

public static String getMd5Hash(String input) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger number = new BigInteger(1, messageDigest);
        String md5 = number.toString(16);

        while (md5.length() < 32)
            md5 = "0" + md5;

        return md5;
    } catch (NoSuchAlgorithmException e) {
        Log.e("MD5", e.getLocalizedMessage());
        return null;
    }
}}
