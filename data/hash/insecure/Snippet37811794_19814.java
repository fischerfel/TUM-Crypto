public static String getFileMd5(String filePath) throws NoSuchAlgorithmException, IOException {
    AutomationLogger.getLog().info("Trying getting MD5 hash from file: " + filePath);
    MessageDigest md = MessageDigest.getInstance("MD5");
    InputStream inputStream;
    try {
        inputStream = Files.newInputStream(Paths.get(filePath));
    } catch (NoSuchFileException e) {
        AutomationLogger.getLog().error("No such file path: " + filePath, e);
        return null;
    }

    DigestInputStream dis = new DigestInputStream(inputStream, md);
    byte[] buffer = new byte[8 * 1024];

    while (dis.read(buffer) != -1);
    dis.close();
    inputStream.close();

    byte[] output = md.digest();
    BigInteger bi = new BigInteger(1, output);
    String hashText = bi.toString(16);
    return hashText;
}
