 public static byte[] getPassword(String value) {
    try {
        return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        throw new RuntimeException(e);
    }
}
