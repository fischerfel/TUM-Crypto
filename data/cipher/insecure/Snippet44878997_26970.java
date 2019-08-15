private static final String ALGORITHM = "AES";
    public static byte[] encrypt(byte[] str) {
    try {
        SecretKeySpec secretKey = new SecretKeySpec("MZygpewJsCpRrfOr".getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(str);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
public static byte[] decrypt(byte[] str) {
    try {
        SecretKeySpec secretKey = new SecretKeySpec("MZygpewJsCpRrfOr".getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        return cipher.doFinal(str);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
