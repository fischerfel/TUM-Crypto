public class Aes {
    private static final String KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final String KEY_SPEC_ALGORITHM = "AES";
    private static final int KEY_LENGTH = 192;
    private static final int KEY_ITERATION_COUNT = 2;

    public static String key  = "dynamicweb";
    public static String salt = "dwapac2015";
    public static String cipherTransformation = "AES/CBC/PKCS5Padding";
    public static String initializationVector = "dwdevelopmentsmm";

    public static String encrypt(String payload) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), KEY_ITERATION_COUNT, KEY_LENGTH);
        SecretKeySpec secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), KEY_SPEC_ALGORITHM);

        Cipher cipher = Cipher.getInstance(cipherTransformation);

        cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(initializationVector.getBytes()));

        byte[] encrypted = cipher.doFinal(payload.getBytes());
        return new String(Base64.encodeBase64(encrypted));
    }
}
