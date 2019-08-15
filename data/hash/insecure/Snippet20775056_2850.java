private static byte[] createChecksum(String filename) throws Exception {
    InputStream fis =  new FileInputStream(filename);

    byte[] buffer = new byte[1024];
    MessageDigest complete = MessageDigest.getInstance("MD5");
    int numRead;

    do {
        numRead = fis.read(buffer);
        if (numRead > 0) {
            complete.update(buffer, 0, numRead);
        }
    } while (numRead != -1);

    fis.close();
    return complete.digest();
}
