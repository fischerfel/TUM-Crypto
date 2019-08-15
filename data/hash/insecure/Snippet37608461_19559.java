private String getHash(String string) {
    try {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] bytes = string.getBytes();
        messageDigest.update(bytes);
        return new BigInteger(1, messageDigest.digest()).toString(36);
    } catch (Exception e) {
        throw new RuntimeException("Could not hash input string.", e);
    }
}
