public static byte[] createChecksum(File log, String type) throws Exception {
    DataInputStream fis = new DataInputStream(new FileInputStream(log));
    Long len = log.length();
    byte[] buffer = new byte[len.intValue()];
    fis.readFully(buffer); // TODO: readFully may come at the risk of
                            // choking on a huge file.
    fis.close();
    MessageDigest complete = MessageDigest.getInstance(type);
    complete.update(buffer);
    return complete.digest();
}
