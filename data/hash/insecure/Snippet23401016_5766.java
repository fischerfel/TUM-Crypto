try {
    MessageDigest digest = MessageDigest.getInstance("SHA-1");
    ...
} catch (NoSuchAlgorithmException e) {
    throw new AssertionError(e);
}
