public class TokenEncryptor {

    private final static String TOKEN_KEY = "91a29fa7w46d8x41";

    public static String encrypt(String plain) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(new byte[16]);
            SecretKeySpec newKey = new SecretKeySpec(TOKEN_KEY.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
            return new String(cipher.doFinal(plain.getBytes()));
        } catch (Exception e) {
            Ln.e(e);
            return null;
        }
    }

    public static String decrypt(String encoded) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(new byte[16]);
            SecretKeySpec newKey = new SecretKeySpec(TOKEN_KEY.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            return new String(cipher.doFinal(encoded.getBytes()));
        } catch (Exception e) {
            Ln.e(e);
            return null;
        }
    }
}
