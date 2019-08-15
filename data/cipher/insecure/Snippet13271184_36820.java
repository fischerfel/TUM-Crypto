public byte[] decrypt(Key key,byte[] textCryp){
    Cipher cipher;
    byte[] decrypted = null;
    try {
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        decrypted = cipher.doFinal(textCryp);
    } catch (Exception e) {         
        e.printStackTrace();
    } 

    return decrypted;
}
