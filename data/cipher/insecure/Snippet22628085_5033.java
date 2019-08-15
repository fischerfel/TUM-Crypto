public class Cryptography {

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA");
        digest.update("BhLKTyLoP YroUsRQT".getBytes());
        return new SecretKeySpec(digest.digest(), 0, 16, "AES");
    }

    public static byte[] encrypt(String message, SecretKey key) throws NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException {
        Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        aes.init(Cipher.ENCRYPT_MODE, key);
        return aes.doFinal(message.getBytes());
    }

    public static String decrypt(byte[] cipherText, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        aes.init(Cipher.DECRYPT_MODE, key);
        return new String(aes.doFinal(cipherText));
    }

}
