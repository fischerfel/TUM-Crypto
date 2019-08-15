private static final String ALGORITHM = "AES";
private static final byte[] keyValue = getKeyBytes("12345678901234567890123456789012");

private static final byte[] INIT_VECTOR = new byte[16];
private static IvParameterSpec ivSpec = new IvParameterSpec(INIT_VECTOR);

public static void main(String[] args) throws Exception {
    String encoded = encrypt("watson?");
    System.out.println(encoded);
}

private static Key generateKey() throws Exception {
    Key key = new SecretKeySpec(keyValue, ALGORITHM);
    // SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
    // key = keyFactory.generateSecret(new DESKeySpec(keyValue));
    return key;
}

private static byte[] getKeyBytes(String key) {
    byte[] hash = DigestUtils.sha(key); // key.getBytes()
    byte[] saltedHash = new byte[16];
    System.arraycopy(hash, 0, saltedHash, 0, 16);
    return saltedHash;
}

public static String encrypt(String valueToEnc) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    c.init(Cipher.ENCRYPT_MODE, key,ivSpec);
    byte[] encValue = c.doFinal(valueToEnc.getBytes());
    String encryptedValue = new BASE64Encoder().encode(encValue);
    return encryptedValue;
}

public static String decrypt(String encryptedValue) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance(ALGORITHM);
    c.init(Cipher.DECRYPT_MODE, key);
    byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
    byte[] decValue = c.doFinal(decordedValue);
    String decryptedValue = new String(decValue);
    return decryptedValue;
}
