public static String toSHA1(byte[] convertme) {
    md = MessageDigest.getInstance("SHA-1");
    return Base64.getEncoder().encodeToString((md.digest(convertme));
}
