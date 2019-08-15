public String encodeString(String s) {
    try {
        // Create MD5 Hash
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        digest.update(s.getBytes());
        byte messageDigest[] = digest.digest();

        Base64 b = null;

        return b.encodeToString(messageDigest,1); 

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return "";
}
