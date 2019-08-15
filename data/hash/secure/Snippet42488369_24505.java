public static String fileSha256ToBase64(File file) throws NoSuchAlgorithmException, IOException {
    byte[] data = Files.readAllBytes(file.toPath());
    MessageDigest digester = MessageDigest.getInstance("SHA-256");
    digester.update(data);
    return Base64.getEncoder().encodeToString(digester.digest());
}
