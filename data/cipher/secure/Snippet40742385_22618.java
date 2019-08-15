public class EncryptionDecryption {

    private static String salt;
    private static int iterations = 65536  ;
    private static int keySize = 256;
    private static byte[] ivBytes;
    private static String encodedKey = "jnf3guertsiAqwecdh7azitonderhascrui5godferh";
    private static SecretKey secretKey;
    public static int checkFlag = 0;

    public static String generateKey() throws Exception { //code for generating key

        char[] chars = "abcdefghijklmnopqrstuvwxyzQWERTYUIOPLKJHGFDSAZXCVBNM1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        char[] plaintext = output.toCharArray();

         salt = getSalt();
         byte[] saltBytes = salt.getBytes();

         SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
         PBEKeySpec spec = new PBEKeySpec(plaintext, saltBytes, iterations, keySize);
         secretKey = skf.generateSecret(spec);

         String newkey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
         return newkey;         
    }

    public static String encrypt(char[] plaintext, String theKey) throws Exception { //code for encrypting

        byte[] decodedKey = Base64.getDecoder().decode(theKey);            
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 

        SecretKeySpec secretSpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretSpec);
        AlgorithmParameters params = cipher.getParameters();
        if(checkFlag == 0){
            ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
            checkFlag = 1;
        }
        byte[] encryptedTextBytes = cipher.doFinal(String.valueOf(plaintext).getBytes("UTF-8"));

        return DatatypeConverter.printBase64Binary(encryptedTextBytes);
    }

    public static String decrypt(char[] encryptedText, String theKey) throws Exception { //code for decrypting
        salt = getSalt();            

        byte[] encryptedTextBytes = DatatypeConverter.parseBase64Binary(new String(encryptedText));
        byte[] decodedKey = Base64.getDecoder().decode(theKey);
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 
        SecretKeySpec secretSpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretSpec, new IvParameterSpec(ivBytes));

        byte[] decryptedTextBytes = null;

        try {
            decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
        }   catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }   catch (BadPaddingException e) {
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
