public class Encryptor {

    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue =
            new byte[] { 'M', 'y', 'S', 'u', 'p', 'e', 'r', 'S',
            'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };

    public static String encrypt(String valueToEnc) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = cipher.doFinal(valueToEnc.getBytes());
        return new BASE64Encoder().encode(encValue);
    }

    public static String decrypt(String encryptedValue) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
        byte[] decValue = cipher.doFinal(decodedValue);
        return new String(decValue);
    }

    private static Key generateKey() throws Exception {
        return new SecretKeySpec(keyValue, ALGORITHM);
    }
}  
