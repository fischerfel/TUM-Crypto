public class PasswordEncryption {
    private final static int DerivationIterations = 1000;
     private final int Keysize = 256;

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[32];
        sr.nextBytes(salt);
        return salt;
    }

    public static String Encrypt(String plainText, String passPhrase)
             {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        byte[] saltStringBytes = getSalt();
        byte[] IvStringBytes=getSalt();
        byte[] pwd = plainText.getBytes("UTF-8");
        char[] chars = passPhrase.toCharArray();
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec pbeKeySpec = new PBEKeySpec(chars, saltStringBytes, DerivationIterations, 256);
        SecretKey secretKey = factory.generateSecret(pbeKeySpec);
        System.out.println(secretKey.toString());
        SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] result = cipher.doFinal(pwd);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(saltStringBytes);
        outputStream.write(IvStringBytes);
        outputStream.write(result);

        byte end[] = outputStream.toByteArray();

        String base64Encoded = Base64.getEncoder().encodeToString(end);
        System.out.println(base64Encoded);

        return base64Encoded;

    }

    public static void main(String[] args) {
        Encrypt("passwordhere", "ABC");
    }

}
