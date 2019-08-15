public class AES {

    private static final String password = "encrypt";
    private static String salt;
    private static final int pswdIterations = 65536;
    private static final int keySize = 128;
    private byte[] ivBytes;

    public String encrypt(String plainText) throws Exception {

        //get salt
        salt = generateSalt();
        byte[] saltBytes = salt.getBytes("UTF-8");

        // Derive the key
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                saltBytes,
                pswdIterations,
                keySize
        );

        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        //encrypt the message
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return new Base64().encodeAsString(encryptedTextBytes);
    }

    @SuppressWarnings("static-access")
    public String decrypt(String encryptedText) throws Exception {

        byte[] saltBytes = salt.getBytes("UTF-8");
        byte[] encryptedTextBytes = new Base64().decodeBase64(encryptedText);

        // Derive the key
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                saltBytes,
                pswdIterations,
                keySize
        );

        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        // Decrypt the message
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));

        byte[] decryptedTextBytes = null;
        try {
            decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
        }

        return new String(decryptedTextBytes);
    }

    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        String s = new String(bytes);
        return s;
    }
}
