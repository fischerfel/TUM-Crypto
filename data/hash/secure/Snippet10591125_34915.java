public String encryptPassword(String passwordInClear) {
            // Salt all you want here.
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
    byte[] digest = sha256.digest(passwordInClear.getBytes());
    return digestToString(digest);
}

private String digestToString(byte[] digest) {
    StringBuilder hashString = new StringBuilder();
    for (int i = 0; i < digest.length; i++) {
        String hex = Integer.toHexString(digest[i]);
        if (hex.length() == 1) {
            hashString.append('0');
            hashString.append(hex.charAt(hex.length() - 1));
        } else {
            hashString.append(hex.substring(hex.length() - 2));
        }
    }
    return hashString.toString();
}
