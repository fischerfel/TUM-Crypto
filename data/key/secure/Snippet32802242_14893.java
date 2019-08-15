private static final String PADDING = "DESede/ECB/PKCS5Padding";
private static final String UTF_F8 = "UTF-8";
private static final String DE_SEDE = "DESede";
private String secretKey;


public String encrypt(String message) throws Exception {

    secretKey = getSecretKey();

    final byte[] secretBase64Key = Base64.decodeBase64(secretKey);
    final SecretKey key = new SecretKeySpec(secretBase64Key, DE_SEDE);
    final Cipher cipher = Cipher.getInstance(PADDING);
    cipher.init(Cipher.ENCRYPT_MODE, key);
    final byte[] plainTextBytes = message.getBytes();
    final byte[] cipherText = cipher.doFinal(plainTextBytes);

    return Hex.encodeHexString(cipherText);
}

public String decrypt(String keyToDecrypt) throws Exception {

    secretKey = getSecretKey();

    byte[] message = DatatypeConverter.parseHexBinary(keyToDecrypt);
    final byte[] secretBase64Key = Base64.decodeBase64(secretKey);
    final SecretKey key = new SecretKeySpec(secretBase64Key, DE_SEDE);
    final Cipher decipher = Cipher.getInstance(PADDING);
    decipher.init(Cipher.DECRYPT_MODE, key);
    final byte[] plainText = decipher.doFinal(message);

    return new String(plainText, UTF_F8);
}
