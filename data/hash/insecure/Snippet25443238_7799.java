public String md5(String toConvert) {

    String retVal = "";

    MessageDigest algorithm;
    try {
        algorithm = MessageDigest.getInstance("MD5");
        algorithm.reset();
        algorithm.update(toConvert.getBytes());
        byte messageDigest[] = algorithm.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
        }
        retVal = hexString + "";
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return retVal;
}
