public String encryptText(String plainText) throws Exception {
    byte[] plaintext = plainText.getBytes("UTF-8");//input
    byte[] tdesKeyData =  "sdfsdgf3q453qdsg".getBytes("UTF-8"); 

    Cipher c3des = Cipher.getInstance("DESede/ECB/PKCS7Padding");
    SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");

    c3des.init(Cipher.ENCRYPT_MODE, myKey);
    byte[] cipherText = c3des.doFinal(plaintext);
    String encryptedString = Base64.encodeToString(cipherText,
            Base64.DEFAULT);
    return encryptedString;
}

public String decrypt(String tekst) throws Exception {

    byte[] tekst2 = tekst.getBytes("UTF-8");
    byte[] tdesKeyData =  "sdfsdgf3q453qdsg".getBytes("UTF-8");
    Cipher c3des = Cipher.getInstance("DESede/ECB/PKCS7Padding");
    SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
    String decryptedString="";


    try {
        c3des.init(Cipher.DECRYPT_MODE, myKey);
        byte[] cipherText = c3des.doFinal(tekst2);
        decryptedString = Base64.decode(cipherText, Base64.DEFAULT).toString();
    }
    catch(Exception e)
    {
        decryptedString=e.getMessage();
    }
    return  decryptedString;
}
