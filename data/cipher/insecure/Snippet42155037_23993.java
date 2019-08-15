public static String decrypt(String strEncrypted,String strKey) throws Exception{
    String strData="";

    try {
        SecretKeySpec skeyspec=new SecretKeySpec(strKey.getBytes(),"Blowfish");
        Cipher cipher=Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, skeyspec);
        byte[] decrypted=cipher.doFinal(strEncrypted.getBytes());
        strData=new String(decrypted);

    } catch (Exception e) {
        e.printStackTrace();
        throw new Exception(e);
    }
    return strData;
}
