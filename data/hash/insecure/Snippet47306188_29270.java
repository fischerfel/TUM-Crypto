private class MultiHttpClientConnThread extends Thread {
    private final Logger logger = Logger.getLogger(getClass());
    private final CloseableHttpClient client;
    private final HttpGet get;
    private final String md5sum;
    private File destinationFile;

    public MultiHttpClientConnThread(final CloseableHttpClient client, final HttpGet get, final File destinationFile) {
        this.client = client;
        this.get = get;
        this.destinationFile = destinationFile;
    }

    @Override
    public final void run() {
        try {
            logger.debug("Thread Running: " + getName());

            CloseableHttpResponse response = client.execute(get);

            String contentRange = response.getFirstHeader("Content-Range").getValue();
            Long startByte = Long.parseLong(contentRange.split("[ -]")[1]);

            Long length = response.getEntity().getContentLength();

            InputStream inputStream = response.getEntity().getContent();

            ReadableByteChannel readableByteChannel;

            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest);
            readableByteChannel = Channels.newChannel(digestInputStream);

            RandomAccessFile randomAccessFile = new RandomAccessFile(destinationFile, "rw");
            FileChannel fileChannel = randomAccessFile.getChannel();

            fileChannel.transferFrom(readableByteChannel, startByte, length);

            md5sum = Hex.encodeHexString(messageDigest.digest());
            logger.info("Part MD5 sum: " + md5sum);

            logger.debug("Thread Finished: " + getName());

            response.close();
            fileChannel.close();
            randomAccessFile.close();
        } catch (final ClientProtocolException ex) {
            logger.error("", ex);
        } catch (final IOException ex) {
            logger.error("", ex);
        } catch (final NoSuchAlgorithmException ex) {
            logger.error("", ex);
        }
    }
}
