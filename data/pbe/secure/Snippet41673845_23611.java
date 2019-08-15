byte[] a = encryptFIN128AES("pls");
String b = decryptFIN128AES(a);
Log.e("AES_Test", "b = " + b);


/**
 * Encrypts a string with AES (128 bit key)
 * @param fin 
 * @return the AES encrypted byte[]
 */
private byte[] encryptFIN128AES(String fin) {

    SecretKeySpec sks = null;

    try {
        sks = new SecretKeySpec(generateKey("Test1".toCharArray(), "Test2".getBytes()).getEncoded(),"AES");
    } catch (Exception e) {
        Log.e("encryptFIN128AES", "AES key generation error");
    }

    // Encode the original data with AES
    byte[] encodedBytes = null;
    try {
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, sks);
        encodedBytes = c.doFinal(fin.getBytes());
    } catch (Exception e) {
        Log.e("encryptFIN128AES", "AES encryption error");
    }

    return encodedBytes;

}


/**
 * Decrypts a string with AES (128 bit key)
 * @param encodedBytes
 * @return the decrypted String
 */
private String decryptFIN128AES(byte[] encodedBytes) {

    SecretKeySpec sks = null;

    try {
        sks = new SecretKeySpec(generateKey("Test1".toCharArray(), "Test2".getBytes()).getEncoded(),"AES");
    } catch (Exception e) {
        Log.e("decryptFIN128AES", "AES key generation error");
    }

    // Decode the encoded data with AES
    byte[] decodedBytes = null;
    try {
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, sks);
        decodedBytes = c.doFinal(encodedBytes);
    } catch (Exception e) {
        Log.e("decryptFIN128AES", "AES decryption error");
    }

    return Base64.encodeToString(decodedBytes, Base64.DEFAULT);
}


public static SecretKey generateKey(char[] passphraseOrPin, byte[] salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException {

    final int iterations = 1000;

    // Generate a 256-bit key
    final int outputKeyLength = 128;

    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations, outputKeyLength);
    SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
    return secretKey;
}
