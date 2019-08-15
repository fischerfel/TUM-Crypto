private static final String ALGO = "AES";
private static final byte[] keyValue = new byte[] { 'o', 'n', 'e', 'n','e', 't', 'e','d', 'o', 'c', 'e', 'i', 'r', 's', 'r', 'p' };
    public static String decrypt(String encryptedData){
        String decryptedValue = null;
        try{
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        decryptedValue = new String(decValue);
    }catch(Exception e){
        //LOGGER.error("In TD:" + e);
        //Teneno_StartupService.loadForConnectionFailed();
    }
    return decryptedValue;
}
private static Key generateKey(){
    Key key = new SecretKeySpec(keyValue, ALGO);
    return key;
}
