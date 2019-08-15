public class EncryptionManager {
    private static final byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };
    private static final byte[] ivBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };

    public byte[] encrypt(byte[] plaintext) throws Exception {
        Cipher cipher = getCipher(true);
        return cipher.doFinal(plaintext);
    }

    public byte[] decrypt(byte [] ciphertext) throws Exception {
        Cipher cipher = getCipher(false);
        return cipher.doFinal(ciphertext);
    }

    private static Cipher getCipher(boolean encrypt) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
        cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE,  
                    secretKeySpec, ivParameterSpec);
        return cipher;
    }
}
