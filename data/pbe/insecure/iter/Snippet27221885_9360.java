/**
 * Encrypt data
 * 
 * @param secretKey
 *            - a secret key used for encryption
 * @param data
 *            - data to encrypt
 * @return Encrypted data
 * @throws Exception
 */
public String cipher(String secretKey, String data) throws Exception {
    SecretKeyFactory factory = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(secretKey.toCharArray(),
            secretKey.getBytes(), 128, 256);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey key = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return toHex(cipher.doFinal(data.getBytes()));
}

/**
 * Decrypt data
 * 
 * @param secretKey
 *            - a secret key used for decryption
 * @param data
 *            - data to decrypt
 * @return Decrypted data
 * @throws Exception
 */
public String decipher(String secretKey, String data) throws Exception {
    SecretKeyFactory factory = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(secretKey.toCharArray(),
            secretKey.getBytes(), 128, 256);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey key = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, key);
    return new String(cipher.doFinal(toByte(data)));
}

// Helper methods

private byte[] toByte(String hexString) {
    int len = hexString.length() / 2;
    byte[] result = new byte[len];
    for (int i = 0; i < len; i++)
        result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                16).byteValue();
    return result;
}

public String toHex(byte[] stringBytes) {
    StringBuffer result = new StringBuffer(2 * stringBytes.length);
    for (int i = 0; i < stringBytes.length; i++) {
        result.append(HEX.charAt((stringBytes[i] >> 4) & 0x0f)).append(
                HEX.charAt(stringBytes[i] & 0x0f));
    }
    return result.toString();
}

private final static String HEX = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
