    public class AESEncDec {

     private static final String ALGO = "AES";
    private static final byte[] keyValue =  new byte[] { 'T', 'h', 'e', 'B','e', 's', 't','S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };


public static String encrypt(String Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        System.err.println("encVal: "+encryptedValue.length());

        return encryptedValue;
    }

    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        //byte[] decValue = c.doFinal(encryptedData.getBytes());
        String decryptedValue = new String(decValue);
        System.err.println("decVal: "+decryptedValue.length());

        return decryptedValue;
    }
    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
}

}
