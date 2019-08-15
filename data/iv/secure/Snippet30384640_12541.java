public class TokenEncryptor {

    private final static String TOKEN_KEY = "fqJfdzGDvfwbedsKSUGty3VZ9taXxMVw";

    public static String encrypt(String plain) {
        try {
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(TOKEN_KEY.getBytes("utf-8"), "AES"), new IvParameterSpec(iv));
            byte[] cipherText = cipher.doFinal(plain.getBytes("utf-8"));
            byte[] ivAndCipherText = getCombinedArray(iv, cipherText);
            return Base64.encodeToString(ivAndCipherText, Base64.NO_WRAP);
        } catch (Exception e) {
            Ln.e(e);
            return null;
        }
    }

    public static String decrypt(String encoded) {
        try {
            byte[] ivAndCipherText = Base64.decode(encoded, Base64.NO_WRAP);
            byte[] iv = Arrays.copyOfRange(ivAndCipherText, 0, 16);
            byte[] cipherText = Arrays.copyOfRange(ivAndCipherText, 16, ivAndCipherText.length);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(TOKEN_KEY.getBytes("utf-8"), "AES"), new IvParameterSpec(iv));
            return new String(cipher.doFinal(cipherText), "utf-8");
        } catch (Exception e) {
            Ln.e(e);
            return null;
        }
    }

    private static byte[] getCombinedArray(byte[] one, byte[] two) {
        byte[] combined = new byte[one.length + two.length];
        for (int i = 0; i < combined.length; ++i) {
            combined[i] = i < one.length ? one[i] : two[i - one.length];
        }
        return combined;
    }

}
