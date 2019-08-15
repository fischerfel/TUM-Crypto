    private static byte[] hashFile(File file) throws Exception {

    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    FileInputStream fis = new FileInputStream(file.getAbsolutePath());

    byte[] bytesBuffer = new byte[1024];
    int bytesRead = 0;

    while ((bytesRead = fis.read(bytesBuffer)) != -1) {
        digest.update(bytesBuffer, 0, bytesRead);
    }

    byte[] hashed = digest.digest();
    return hashed;
}
