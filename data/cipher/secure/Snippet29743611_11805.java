public class tmp{
    static Cipher encryptionCipher;
    static String RANDOM_ALGORITHM = "SHA1PRNG";
    static String PBE_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";
    static String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";
    static String SECRET_KEY_ALGORITHM = "AES";
    static int PBE_ITERATION_COUNT = 2048;
    static String PROVIDER = "BC";

    public static byte[] generateIv() {
        try{
        SecureRandom random;
        random = SecureRandom.getInstance(RANDOM_ALGORITHM);
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        return iv;
        } catch(Exception e){
        return null;            // Always must return something
        }
    }

    public static byte[] generateSalt() {
         try {SecureRandom random;
         random = SecureRandom.getInstance(RANDOM_ALGORITHM);
         byte[] salt = new byte[32];
         random.nextBytes(salt);
         return salt;
         } catch(Exception e){
        return null;            // Always must return something
    }
     }

     public static SecretKey getSecretKey(String password, byte[] salt){
         try {
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, PBE_ITERATION_COUNT, 256);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(PBE_ALGORITHM, PROVIDER);
            SecretKey tmp = factory.generateSecret(pbeKeySpec);
            return new SecretKeySpec(tmp.getEncoded(), SECRET_KEY_ALGORITHM);
        } catch(Exception e){
        System.out.println(e);            // Always must return something
        return null;
        }        
     }

     public static String encrypt(String plaintext, Key key, byte[] iv) {
        try {
            AlgorithmParameterSpec ivParamSpec = new IvParameterSpec(iv);
            encryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM, PROVIDER);
            encryptionCipher.init(Cipher.ENCRYPT_MODE, key, ivParamSpec);
            byte[] ciphertext = encryptionCipher.doFinal(plaintext.getBytes("UTF-8"));
            String cipherHexString = DatatypeConverter.printHexBinary(ciphertext);
            return cipherHexString;
        }
        catch (Exception e) {
             System.out.println(e);
            return null;
        }

    }

     public static void main (String[] Args){
         SecretKey key;
         //sha512(ciao)
         String encami = "This is a test pharse. Thanks!!";
         String password = "a0c299b71a9e59d5ebb07917e70601a3570aa103e99a7bb65a58e780ec9077b1902d1dedb31b1457beda595fe4d71d779b6ca9cad476266cc07590e31d84b206";
         byte[] iv = new byte[16];
         byte[] salt = new byte[32];
         iv = generateIv();
         salt = generateSalt();
         String ll = DatatypeConverter.printHexBinary(iv);
         String lp = DatatypeConverter.printHexBinary(salt);
         System.out.println(ll);
         System.out.println(lp);
         key = getSecretKey(password, salt);
         byte tt[] = new byte[32];
         tt = key.getEncoded();
         String lo = DatatypeConverter.printHexBinary(tt);
         System.out.println(lo);
         String outenc = encrypt(encami, key, iv);
         System.out.println(outenc);
     }
}
