public static String md5(String input) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        BigInteger md5Data = new BigInteger(1, md.digest(input.getBytes()));
        return String.Format("%032X", md5Data);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        return null;
    }
}
