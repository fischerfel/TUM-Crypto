public static String md5(String stringToDigest) throws NoSuchAlgorithmException {
    if(stringToDigest == null) {
            return "";
    }
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] s = md.digest(stringToDigest.getBytes());
    HexBinaryAdapter hba = new HexBinaryAdapter();
    String md5 = hba.marshal(s);
    return md5;
}
