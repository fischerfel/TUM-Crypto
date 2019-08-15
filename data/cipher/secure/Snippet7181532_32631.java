public class DesEncrypter {

    public static final int SALT_LENGTH = 20;
    public static final int PBE_ITERATION_COUNT = 1024;

    private static final String RANDOM_ALGORITHM = "SHA1PRNG";
    private static final String PBE_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";


    public byte[] encrypt(String password, String cleartext) {

        byte[] encryptedText = null;

        try {
            byte[] salt = "dfghjklpoiuytgftgyhj".getBytes();

            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, PBE_ITERATION_COUNT, 256);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWITHSHAAND256BITAES-CBC-BC");

            SecretKey tmp = factory.generateSecret(pbeKeySpec);

            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            byte[] key = secret.getEncoded();

            Cipher encryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM);   

            byte[] iv = generateIv();

            IvParameterSpec ivspec = new IvParameterSpec(iv);

            encryptionCipher.init(Cipher.ENCRYPT_MODE, secret, ivspec);

            encryptedText = encryptionCipher.doFinal(cleartext.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedText;
    }

    public String decrypt(String password, byte[] encryptedText) {

        String cleartext = "";

        try {
            byte[] salt = "dfghjklpoiuytgftgyhj".getBytes();

            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, PBE_ITERATION_COUNT, 256);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWITHSHAAND256BITAES-CBC-BC");

            SecretKey tmp = factory.generateSecret(pbeKeySpec);

            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            byte[] key = secret.getEncoded();

            Cipher decryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM);

            byte[] iv = generateIv();

            IvParameterSpec ivspec = new IvParameterSpec(iv);

            decryptionCipher.init(Cipher.DECRYPT_MODE, secret, ivspec);

            byte[] decryptedText = decryptionCipher.doFinal(encryptedText);

            cleartext =  new String(decryptedText); 

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cleartext;
    }   

    private byte[] generateIv() throws NoSuchAlgorithmException {

        SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);

        byte[] iv = new byte[16];

        random.nextBytes(iv);

        return iv;
    }

}
