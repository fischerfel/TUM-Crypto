public static String encode(final String clearText) throws NoSuchAlgorithmException {
    return new String(
            Base64.getEncoder().encode(MessageDigest.getInstance("SHA-256").digest(clearText.getBytes(StandardCharsets.UTF_8))));
}
