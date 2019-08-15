public static final byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B', 'e',
        's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };
public final static String ALGO = "AES";

public static String Dcrypt(String encryptedData) {
    Key key = new SecretKeySpec(keyValue, ALGO);
    // Key key = generateKey();
    try {
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.decodeBase64(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        decryptedValue = new String(decValue);
    } catch (Exception e) {
    }
    return decryptedValue;
}
