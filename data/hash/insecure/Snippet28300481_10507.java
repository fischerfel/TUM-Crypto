private static String hashCode(String userCode) {
    String hashSha1;
    MessageDigest digest = null;
    try {
        digest = MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    digest.reset();
    byte[] data = digest.digest(userCode.getBytes());
    return Base64.encodeToString(data, Base64.DEFAULT);
}
