public class testMain {
    private static byte[] key = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".getBytes();
    private static byte[] plaintext = "fjaiejrfoi".getBytes();
    private static Cipher cipher;
    private static byte[] result;

    public static void main(String[] args) {
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        SecretKeySpec secretKey = null;
        try {
            secretKey = makeKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            result = cipher.doFinal(plaintext);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer();
        for(byte b : result) {
            sb.append(Integer.toHexString((int)(b&0xff)));
        }
        System.out.println("result is " + sb.toString());

    }

    private static SecretKeySpec makeKey() throws NoSuchAlgorithmException{
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(256, new SecureRandom(key));
        SecretKey secretKey = kgen.generateKey();
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
        return keySpec;
    }
}
