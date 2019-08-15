public class Decrypts {
    private static final String password = "haasd";
    private static final String ALGO = "AES";

    public static String encrypt(String Data) throws Exception {
        String SALT = generateSalt();
        byte[] saltBytes = SALT.getBytes("UTF-8");
        System.out.println("Saltbytes ; " + saltBytes);

        SecretKeyFactory factory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes,
                65536, 256);

        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, secret);
        byte[] encVal = c.doFinal(Data.getBytes());
        byte[] encryptedValue = new Base64().encode(encVal);
        String ency = new String(encryptedValue);
        return ency;
    }

    @SuppressWarnings("static-access")
    public static String decrypt(String encryptedData) throws Exception {
        String SALT = generateSalt();
        byte[] saltBytes = SALT.getBytes("UTF-8");
        System.out.println("Saltbytes ; " + saltBytes);

        SecretKeyFactory factory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes,
                65536, 256);

        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, secret);
        byte[] decordedValue = new Base64().decodeBase64(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom(password.getBytes());
        byte bytes[] = new byte[120];
        random.nextBytes(bytes);
        String s = new String(bytes);
        System.out.println("Salt ; " + s);
        return s;
    }
}
