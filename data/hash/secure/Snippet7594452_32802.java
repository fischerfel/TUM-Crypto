    public static boolean fileContentEquals(String filePathA, String filePathB) throws Exception {
    if (!compareFilesLength(filePathA, filePathB)) return false;

    BufferedInputStream streamA = null;
    BufferedInputStream streamB = null;
    try {
        File fileA = new File(filePathA);
        File fileB = new File(filePathB);

        streamA = new BufferedInputStream(new FileInputStream(fileA));
        streamB = new BufferedInputStream(new FileInputStream(fileB));

        int chunkSizeInBytes = 16384;
        byte[] bufferA = new byte[chunkSizeInBytes];
        byte[] bufferB = new byte[chunkSizeInBytes];

        int totalReadBytes = 0;
        while (totalReadBytes < fileA.length()) {
            int readBytes = streamA.read(bufferA);
            streamB.read(bufferB);

            if (readBytes == 0) break;

            MessageDigest digestA = MessageDigest.getInstance(CHECKSUM_ALGORITHM);
            MessageDigest digestB = MessageDigest.getInstance(CHECKSUM_ALGORITHM);

            digestA.update(bufferA, 0, readBytes);
            digestB.update(bufferB, 0, readBytes);

            if (!MessageDigest.isEqual(digestA.digest(), digestB.digest()))
            {
                closeStreams(streamA, streamB);
                return false;
            }

            totalReadBytes += readBytes;
        }
        closeStreams(streamA, streamB);
        return true;
    } finally {
        closeStreams(streamA, streamB);
    }
}

public static void closeStreams(Closeable ...streams) {
    for (int i = 0; i < streams.length; i++) {
        Closeable stream = streams[i];
        closeStream(stream);
    }
}
public static boolean compareFilesLength(String filePathA, String filePathB) {
    File fileA = new File(filePathA);
    File fileB = new File(filePathB);

    return fileA.length() == fileB.length();
}
private static void closeStream(Closeable stream) {
    try {
        stream.close();
    } catch (IOException e) {
        // ignore exception
    }
}
