public static String getHashMD5(String string) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        BigInteger bi = new BigInteger(1, md.digest(string.getBytes()));
        return bi.toString(16);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(MD5Utils.class
                .getName()).log(Level.SEVERE, null, ex);

        return "";
    }
}
