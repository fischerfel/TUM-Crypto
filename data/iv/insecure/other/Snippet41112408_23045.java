public class AesBase64Wrapper {

    private static String IV = "IV_VALUE_16_BYTE"; 
    private static String PASSWORD = "PASSWORD_VALUE"; 
    private static String SALT = "SALT_VALUE";

    public String encryptAndEncode(String raw) {
        try {
            Cipher c = getCipher(Cipher.ENCRYPT_MODE);
            byte[] encryptedVal = c.doFinal(getBytes(raw));
            String s = getString(Base64.encodeBase64(encryptedVal));
            return s;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private String getString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, "UTF-8");
    }

    private byte[] getBytes(String str) throws UnsupportedEncodingException {
        return str.getBytes("UTF-8");
    }

    private Cipher getCipher(int mode) throws Exception {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = getBytes(IV);
        c.init(mode, generateKey(), new IvParameterSpec(iv));
        return c;
    }

    private Key generateKey() throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        char[] password = PASSWORD.toCharArray();
        byte[] salt = getBytes(SALT);

        KeySpec spec = new PBEKeySpec(password, salt, 2048, 128);
        SecretKey tmp = factory.generateSecret(spec);
        byte[] encoded = tmp.getEncoded();
        return new SecretKeySpec(encoded, "AES");
    }
}
