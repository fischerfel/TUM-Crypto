@Test
public void test06() throws Exception {
    char[] password = "password".toCharArray();
    String s = "an unencryted string to be encrypted and decrypted";
    byte[] sEncrypted = encrypt(password, s);
    String sEncryptedDecrypted = decrypt(password, sEncrypted);
    Assert.assertEquals(s, sEncryptedDecrypted);
}

private static final byte[] KEY_SALT = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e,
        (byte) 0xc8, (byte) 0xee, (byte) 0x99 };
private static final int KEY_ITERATION_COUNT = 1024;
private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
private static final int KEY_LENGTH = 128;
private static final String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA1";
private static final String CIPHER_ALGORITHM = "AES";
private static final String CHARSET_NAME = "UTF-8";
private byte[] initializer;

public byte[] encrypt(final char[] password, final String s) throws Exception {
    KeySpec keySpec = new PBEKeySpec(password, KEY_SALT, KEY_ITERATION_COUNT, KEY_LENGTH);
    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);
    SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
    Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
    SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), CIPHER_ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    initializer = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
    return cipher.doFinal(s.getBytes(CHARSET_NAME));
}

public String decrypt(final char[] password, final byte[] sEncrypted) throws Exception {
    KeySpec keySpec = new PBEKeySpec(password, KEY_SALT, KEY_ITERATION_COUNT, KEY_LENGTH);
    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);
    SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
    Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
    SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), CIPHER_ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(initializer));
    final byte[] sByteArray = cipher.doFinal(sEncrypted);
    return new String(sByteArray, CHARSET_NAME);
}
