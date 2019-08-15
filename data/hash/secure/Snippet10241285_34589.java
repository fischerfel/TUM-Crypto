protected String makeTokenSignature(long tokenExpiryTime, String username, String password) {
    String data = username + ":" + tokenExpiryTime + ":" + password + ":" + getKey();
    MessageDigest digest;
    try {
        digest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException("No SHA-256 algorithm available!");
    }

    return new String(Hex.encode(digest.digest(data.getBytes())));
}
