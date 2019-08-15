public class Encryptionsss {

public static void main(String[] args) throws Exception {

     try {
         String text = "Hello World";
         String key = "1234567891234567";
         // Create key and cipher
         Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
         Cipher cipher = Cipher.getInstance("AES");

     // encrypt the text
     cipher.init(Cipher.ENCRYPT_MODE, aesKey);
     byte[] encrypted = cipher.doFinal(text.getBytes());
     System.out.println("Encrypted text: " + new String(encrypted));

     // decrypt the text
     cipher.init(Cipher.DECRYPT_MODE, aesKey);
     String decrypted = new String(cipher.doFinal(encrypted));
     System.out.println("Decrypted text: " + decrypted);
  }catch(Exception e) {
     e.printStackTrace();
  }

    String plainText = "Hello World";

    /**
     * Generate new Key 
     */
//  String str = generatenewkeyasString();



 /*** Generate Cipher Text from Key(We are using same key stored in String-str)
 ****/


    String str = "]˜??4I-S@æ,Ôt";
    byte[] data = str.getBytes();
    SecretKey key2 = new SecretKeySpec(data, 0, data.length, "AES");
    byte[] cipherText = encryptText(plainText, key2);
    String scipherText = new String(cipherText);
   /**
    *
    * Decrypt Cipher Text with Key****/

    cipherText = scipherText.getBytes();
    String decryptedText = decryptText(cipherText, key2);
    System.out.println("ScipherText:" + scipherText);
    System.out.println("Original Text:" + plainText);
    System.out.println("AES Key (Hex Form):"
            + bytesToHex(key2.getEncoded()));
    System.out.println("Encrypted Text (Hex Form):"
            + bytesToHex(cipherText));
    System.out.println("Descrypted Text:" + decryptedText);

}

/**
 * 
 * @return byte[] as String
 * @Generate Key
 */

private static String generatenewkeyasString() throws Exception {
    SecretKey secKey = KeyGenerator.getInstance("AES").generateKey();
    byte[] data = secKey.getEncoded();
    String str = new String(data);
    return str;

}

/**
 * 
 * Encrypts plainText in AES using the secret key
 * 
 * @param plainText
 * 
 * @param secKey
 * 
 * @return
 * 
 * @throws Exception
 */

public static byte[] encryptText(String plainText, SecretKey secKey)
        throws Exception {

    // AES defaults to AES/ECB/PKCS5Padding in Java 7

    Cipher aesCipher = Cipher.getInstance("AES");
    aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
    byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
    return byteCipherText;

}

/**
 * 
 * Decrypts encrypted byte array using the key used for encryption.
 * 
 * @param byteCipherText
 * @param secKey
 * 
 * @return
 * 
 * @throws Exception
 */

public static String decryptText(byte[] byteCipherText, SecretKey secKey)
        throws Exception {

    // AES defaults to AES/ECB/PKCS5Padding in Java 7

    Cipher aesCipher = Cipher.getInstance("AES");
    aesCipher.init(Cipher.DECRYPT_MODE, secKey);
    byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
    return new String(bytePlainText);

}

/**
 * 
 * Convert a binary byte array into readable hex form
 * 
 * @param hash
 * 
 * @return
 */

private static String bytesToHex(byte[] hash) {
    return DatatypeConverter.printHexBinary(hash);

}
