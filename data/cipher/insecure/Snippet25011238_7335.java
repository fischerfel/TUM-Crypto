import org.apache.commons.codec.binary.Base64;

public class Decrypts {
    private static final String password = "haasd";
    private static final String ALGO = "AES";

    public static String encrypt(String Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        byte[] encryptedValue = new Base64().encode(encVal);
        String ency = new String(encryptedValue);
        return ency;
    }

    @SuppressWarnings("static-access")
    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new Base64().decodeBase64(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static Key generateKey() throws Exception {

        try {
            String salt;
            int pswdIterations = 65536;
            int keySize = 256;
            salt = generateSalt();
            byte[] saltBytes = salt.getBytes("UTF-8");

            SecretKeyFactory factory = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes,
                    pswdIterations, keySize);

            SecretKey secretKey = factory.generateSecret(spec);

            Key key = new SecretKeySpec(secretKey.getEncoded(), "AES");
            return key;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;

    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        String s = new String(bytes);
        return s;
    }

}
