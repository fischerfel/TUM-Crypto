public static boolean matching(String orig, String compare){
    String md5 = null;
    try{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(compare.getBytes());
        byte[] digest = md.digest();
        md5 = new BigInteger(1, digest()).toString(16);

        return md5.equals(orig);

    } catch (NoSuchAlgorithmException e) {
        return false;
    }

    return false;
}
