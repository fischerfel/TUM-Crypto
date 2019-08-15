//encryption function
public static String encryptMsg(byte [] msgBytes, SecretKey myDesKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
{
    Cipher desCipher;
    // Create the cipher 
    desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
    desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
    byte[] textEncrypted = desCipher.doFinal(msgBytes);

// converts to base64 for easier display.
byte[] base64Cipher = Base64.encode(textEncrypted);
return new String(base64Cipher);
} //end encryptMsg
