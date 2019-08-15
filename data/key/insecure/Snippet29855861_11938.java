public class Encryption {
    private static final String ALGO = "AES/CBC/PKCS5Padding";
    private static final byte[] keyValue = new byte[]{'F','O','R','T','Y','T','W','O','F','O','R','T','Y','T','W','O'};
    private static Key key = generateKey();

    public static String encrypt(String DATA) throws Exception {
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE,key,new IvParameterSpec(c.getParameters().getParameterSpec(IvParameterSpec.class).getIV()));
        byte[] encVal = c.doFinal(DATA.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }

    public static String decrypt(String DATA) throws Exception {
        System.out.println(DATA.length());
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(c.getParameters().getParameterSpec(IvParameterSpec.class).getIV()));
        byte[] dencVal = c.doFinal(DATA.getBytes());
        String decryptedValue = new BASE64Encoder().encode(dencVal);
        return decryptedValue;

    }
    public static Key generateKey() {
            Key key = new SecretKeySpec(keyValue,"AES");
            return key;
    }
}
