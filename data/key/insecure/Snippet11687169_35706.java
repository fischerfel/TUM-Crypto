    public static byte[] key_reg = new byte[] {1, 2, 3, 4, 5,6, 7, 8, 9, 1, 2, 3, 4, 5, 6, 7};//... secret sequence of bytes

    public static String getDecryptedString(byte[] data,int i){
    String str = null;
    try {
        Cipher ci = Cipher.getInstance("AES/ECB/PKCS5Padding","SunJCE");
        SecretKeySpec sk =   new SecretKeySpec(key_reg, "AES");
        ci.init(Cipher.DECRYPT_MODE, sk);
        byte[] dataD = ci.doFinal(data);
        str = new String(dataD,"UTF-8");
        return str;
    } catch (Throwable e) {
        logger.error("Error in getRequestString : "+e);
        e.printStackTrace();
    }
    return str;
}
