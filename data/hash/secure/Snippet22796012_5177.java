public static byte[] hash(File file, String hashAlgo) throws IOException {

    FileInputStream inputStream = null;

    try {
        MessageDigest md = MessageDigest.getInstance(hashAlgo);
        inputStream = new FileInputStream(file);
        FileChannel channel = inputStream.getChannel();

        long length = file.length();
        if(length > Integer.MAX_VALUE) {
            // you could make this work with some care,
            // but this code does not bother.
            throw new IOException("File "+file.getAbsolutePath()+" is too large.");
        }

        ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, length);

        int bufsize = 1024 * 8;          
        byte[] temp = new byte[bufsize];
        int bytesRead = 0;

        while (bytesRead < length) {
            int numBytes = (int)length - bytesRead >= bufsize ? 
                                         bufsize : 
                                         (int)length - bytesRead;
            buffer.get(temp, 0, numBytes);
            md.update(temp, 0, numBytes);
            bytesRead += numBytes;
        }

        byte[] mdbytes = md.digest();
        return mdbytes;

    } catch (NoSuchAlgorithmException e) {
        throw new IllegalArgumentException("Unsupported Hash Algorithm.", e);
    }
    finally {
        if(inputStream != null) {
            inputStream.close();
        }
    }
}
