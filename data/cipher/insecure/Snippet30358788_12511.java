public class AESUtils {

    public static byte[] encrypt(SecretKeySpec skeySpec, byte[] clear) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    public static byte[] decrypt(SecretKeySpec skeySpec, byte[] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static SecretKeySpec getKey(String key) throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(key.getBytes());
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128, sr);
        return new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
    }
}
