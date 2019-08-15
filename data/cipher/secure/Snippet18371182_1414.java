    private static final byte[] SALT = {
        (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
        (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
};
private static final int ITERATION_COUNT = 65536;
private static final int KEY_LENGTH = 256;
public static void encryptOrDecrypt(String key, byte[] is, OutputStream os) throws Throwable {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(key.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
    Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    IvParameterSpec ivspec = new IvParameterSpec(iv);
    ecipher.init(Cipher.DECRYPT_MODE, secret, ivspec);

    byte[] encrypted = ecipher.doFinal(is);
    os.write(encrypted);
    os.close();
