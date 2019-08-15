public class Encryption {

    private static int iterations = 65536;
    private static int keySize = 128;
    private static char[] password = "password".toCharArray();
    private static String algorithm= "PBKDF2WithHmacSHA1";


    private static final String SEPARATOR = "~";


     public static void main(String []args) throws Exception {

         String filePath = "test.xml";

         String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));

         String encrMesg = encrypt(fileContent);

         System.out.println("Encrypted: " + encrypt(encrMesg)); 

         System.out.println("Decrypted: " + decrypt(encrMesg)); 
     }


    public static String encrypt(String plaintext) throws Exception {


        byte[] saltBytes = getSalt().getBytes();

        SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
        PBEKeySpec spec = new PBEKeySpec(password, saltBytes, iterations, keySize);
        SecretKey secretKey = skf.generateSecret(spec);
        SecretKeySpec secretSpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretSpec);
        AlgorithmParameters params = cipher.getParameters();

        byte[] ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] cipherText = cipher.doFinal(String.valueOf(plaintext).getBytes("UTF-8"));

        return DatatypeConverter.printBase64Binary(ivBytes)+SEPARATOR+DatatypeConverter.printBase64Binary(saltBytes)
        +SEPARATOR+DatatypeConverter.printBase64Binary(cipherText);
    }

    public static String decrypt(String encryptedText) throws Exception {

        System.out.println(encryptedText);

        String[] encryptedArr = encryptedText.split(SEPARATOR);

        byte[] ivBytes = DatatypeConverter.parseBase64Binary(new String(encryptedArr[0]));

        byte[] salt = DatatypeConverter.parseBase64Binary(new String(encryptedArr[1]));

        byte[] encryptedTextBytes = DatatypeConverter.parseBase64Binary(new String(encryptedArr[2]));

        SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keySize);
        SecretKey secretKey = skf.generateSecret(spec);
        SecretKeySpec secretSpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretSpec, new IvParameterSpec(ivBytes));

        byte[] decryptedTextBytes = null;

        try {
            decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return new String(decryptedTextBytes);

    }

    public static String getSalt() throws Exception {

        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[20];
        sr.nextBytes(salt);
        return new String(salt);
    }

}
