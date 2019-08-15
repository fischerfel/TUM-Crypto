public class AESHelper {

    private static final String TAG = "AESHelper";

    public static byte[] encrypt(byte[] data, String initVector, String key) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecretKeySpec k = new SecretKeySpec(Base64.decode(key, Base64.DEFAULT), "AES");
            c.init(Cipher.ENCRYPT_MODE, k, iv);
            return c.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] decrypt(byte[] data, String initVector, String key) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecretKeySpec k = new SecretKeySpec(Base64.decode(key, Base64.DEFAULT), "AES");
            c.init(Cipher.DECRYPT_MODE, k, iv);
            return c.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String keyGenerator() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(192);

        return Base64.encodeToString(keyGenerator.generateKey().getEncoded(),
                Base64.DEFAULT);
    }
}
