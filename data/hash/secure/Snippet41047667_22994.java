public String hash(String text) throws UnsupportedEncodingException, NoSuchAlgorithmException {
    byte[] b = text.getBytes(StandardCharsets.UTF_8);
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    return new String(digest.digest(b));
}
