private static final String AES_SECRET = "PreDefinedKey";

/**
 * Method for AES encryption
 * @param raw
 * @param plain
 * @return
 * @throws Exception
 */
private static byte[] encrypt(byte[] raw, byte[] plain) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES/ECB/PKCS7Padding");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(plain);
    return encrypted;
}



/**
 * AES decryption
 * @param encryptMsg
 * @return
 * @throws Exception
 */
public static String AESDecrypt(String encryptMsg)
        throws Exception {          
    byte[] rawKey = getRawKey(AES_SECRET.getBytes());
    //byte[] enc = toByte(encryptMsg);
    byte[] enc = Base64.decode(encryptMsg, 0);
    byte[] result = decrypt(rawKey, enc);
    return new String(result);

}

/**
 * Method for AES decryption
 * @param raw
 * @param encrypted
 * @return
 * @throws Exception
 */
private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
    SecretKeySpec keySpec = new SecretKeySpec(raw, "AES/ECB/PKCS7Padding");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
    cipher.init(Cipher.DECRYPT_MODE, keySpec);
    byte[] decrypted = cipher.doFinal(encrypted);
    return decrypted;

}

public static byte[] getRawKey(byte[] seed) throws Exception {

    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    sr.setSeed(seed);
    //Init for 256bit AES key
    kgen.init(256);
    SecretKey secret = kgen.generateKey();
    //Get secret raw key
    byte[] raw = secret.getEncoded();

    return seed;

}
