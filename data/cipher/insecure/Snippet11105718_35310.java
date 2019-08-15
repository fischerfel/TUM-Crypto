public static String encrypt(String plaintext, String enctoken){

    if(enctoken == null)
        enctoken = "sfdjf48mdfdf3054";

    String encrypted = null;

    byte[] plaintextByte = EncodingUtils.getAsciiBytes(plaintext);

    //nel caso non funzionasse provare getBytes semplicemente
    byte[] pwd = EncodingUtils.getAsciiBytes(Connessione.md5(enctoken));        

    try {
        Cipher cipher = Cipher.getInstance("DESEDE/ECB/NoPadding");
        SecretKeySpec myKey = new SecretKeySpec(pwd,"DESede");

        cipher.init( Cipher.ENCRYPT_MODE, myKey);

        try {
            byte[] encryptedPlainText= cipher.doFinal(plaintextByte);

            encrypted = Base64.encodeToString(encryptedPlainText, 0); 
            return encrypted;

        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }       

    return "";
}
