public class Encryption {

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        KeyGenerator keygen = KeyGenerator.getInstance("DESede");
        keygen.init(168);
        SecretKey klucz = keygen.generateKey();

        return klucz;
    }

    static byte[] encrypt(byte[] plainTextByte, SecretKey klucz)
        throws Exception {
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, klucz);
        byte[] encryptedBytes = cipher.doFinal(plainTextByte);
        return encryptedBytes;
    }

    static byte[] decrypt(byte[] encryptedBytes, SecretKey klucz)
        throws Exception {
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, klucz);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return decryptedBytes;
    }
}
