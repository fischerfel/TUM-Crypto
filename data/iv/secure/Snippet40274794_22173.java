public class MysteryHandler {

    private static SecretKey secretKey;
    private static Cipher cipher;
    private static byte[] utf8Bytes;

    /**Verschluesseln*/
    public static byte[] encrypt(String plainText){
        try {
            secretKey = KeyGenerator.getInstance("AES").generateKey();
            cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            utf8Bytes = plainText.getBytes("UTF8");
            return cipher.doFinal(utf8Bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**Entschluesseln*/
    public static String decrypt(byte[] secret){
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(secret, secret.length - 16, 16));
            utf8Bytes = cipher.doFinal(secret);
            return new String(utf8Bytes, "UTF8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
