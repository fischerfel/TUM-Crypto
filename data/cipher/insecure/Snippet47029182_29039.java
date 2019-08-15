public class EncryptUtils {
public static SecretKey generateKey(String mySecret) 
    throws NoSuchAlgorithmException, InvalidKeySpecException 
{ 
    return secret = new SecretKeySpec(mySecret.getBytes(), "AES"); 
}

public static byte[] encryptMsg(String message, SecretKey secret)
    throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException 
{ 
   /* Encrypt the message. */
   Cipher cipher = null; 
   cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
   cipher.init(Cipher.ENCRYPT_MODE, secret); 
   byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8")); 
   return cipherText; 
}

public static String decryptMsg(byte[] cipherText, SecretKey secret) 
    throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException 
{
    /* Decrypt the message, given derived encContentValues and initialization vector. */
    Cipher cipher = null;
    cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secret); 
    String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
    return decryptString; 
}
}

String mySecret="mySecretKeyString";
String secretKey = EncryptUtils.generateKey(mySecret);
String encryptedStr = EncryptUtils.encryptMsg(jsonResultString, secretKey));

String decryptedStr = EncryptUtils.decryptMsg(encryptedStr.getBytes("UTF-8"), secretKey));
