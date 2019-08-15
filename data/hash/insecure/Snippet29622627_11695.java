 public String getHashedPassword(String pstrPassword) throws Exception {
    MessageDigest objMsgDigest;
    try {
        objMsgDigest = MessageDigest.getInstance("SHA-1");
        objMsgDigest.update(pstrPassword.getBytes("UTF-8"));
    } catch (NoSuchAlgorithmException e) {
        throw e;
    } catch (UnsupportedEncodingException e) {
        throw e;
    }
    byte byteHash[] = objMsgDigest.digest();
    String strHashPwd = "";
    for (int i = 0; i < byteHash.length; i++) {

        // for (byte aByteHash : byteHash) {
        strHashPwd += Integer.toHexString(byteHash[i] & 0xff);
    }
    return strHashPwd;
}
