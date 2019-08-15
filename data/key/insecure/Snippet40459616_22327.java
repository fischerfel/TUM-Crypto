public class AESencrp {




private static final String ALGO = "AES";
private static final byte[] keyValue =
        new byte[] { '0', '1', '2', '3', '4', '5', '6',

                '7', '8', '9', '0','1', '2', '3', '4', '5' };


public static String encrypt(String Data) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance(ALGO);
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encVal = c.doFinal(Data.getBytes());
    //String encryptedValue = com.example.xpertech.aesencryption.Base64.encodeToString(encVal, com.example.xpertech.aesencryption.Base64.DEFAULT);
 //   String encryptedValue = new BASE64Encoder().encode(encVal);
    String encryptedValue = Base64.encodeToString(encVal, Base64.NO_WRAP);
    return encryptedValue;
}

public static String decrypt(String encryptedData) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance(ALGO);
    c.init(Cipher.DECRYPT_MODE, key);

   // byte[] decordedValue = new  com.example.xpertech.aesencryption.Base64.Decoder(encryptedData, com.example.xpertech.aesencryption.Base64.NO_WRAP);
    byte[] decordedValue = Base64.decode(encryptedData, Base64.DEFAULT);

   // byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);

    byte[] decValue = c.doFinal(decordedValue);
    String decryptedValue = new String(decValue);
    return decryptedValue;
}
private static Key generateKey() throws Exception {
    Key key = new SecretKeySpec(keyValue, ALGO);
    return key;
}
