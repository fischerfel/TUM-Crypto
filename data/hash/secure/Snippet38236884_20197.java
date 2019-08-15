static byte[] hash(byte[] bytes, final int count)
    throws NoSuchAlgorithmException {
    final MessageDigest digest = MessageDigest.getInstance("SHA-256");
    for (int i = 0; i < count; i++) {
        bytes = digest.digest(bytes);
    }
    return bytes;
}
