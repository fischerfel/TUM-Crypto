public class EncryptionUtil {

    private static final Log LOGGER = LogFactory.getLog(EncryptionUtil.class);
    private static final String CIPHER_MODE = "AES/CBC/PKCS5PADDING";
    private static final String CRYPTO_PROPERTIES_PATH = "/crypto.properties";
    private static final SecretKeySpec sKey = keySpecFromProperties();

    private EncryptionUtil() {}

    public static byte[] encrypt(byte[] aBytes) {
         try {
            SecureRandom lSecureRandom = new SecureRandom();
            byte[] ivBytes = new byte[16];
            lSecureRandom.nextBytes(ivBytes);
            IvParameterSpec lSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, sKey, lSpec);
            byte[] encryptedBytes = cipher.doFinal(aBytes);
            byte[] outBytes = new byte[encryptedBytes.length + 16];
            System.arraycopy(ivBytes, 0, outBytes, 0, 16);
            System.arraycopy(encryptedBytes, 0, outBytes, 16, encryptedBytes.length);

            return outBytes;
        } catch (Exception aEx) {
            LOGGER.error("Failed to encrypt bytes");
            throw new RuntimeException(aEx);
        }
    }

    public static byte[] decrypt(byte[] aBytes) {
        try {
            byte[] lIvBytes = Arrays.copyOfRange(aBytes, aBytes.length - 16, aBytes.length);
            byte[] lEncryptedBytes = Arrays.copyOfRange(aBytes, 0, aBytes.length - 16);
            IvParameterSpec lIvSpec = new IvParameterSpec(lIvBytes);
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);
            cipher.init(Cipher.DECRYPT_MODE, sKey, lIvSpec);
            return cipher.doFinal(lEncryptedBytes);
        }catch (Exception aEx){
            LOGGER.error("Failed to decrypt bytes. Returning input bytes", aEx);
            return aBytes;
        }
    }

    private static SecretKeySpec keySpecFromProperties(){
        try(InputStream lPropStream = EncryptionUtil.class.getResourceAsStream(CRYPTO_PROPERTIES_PATH)){
            Properties cryptoProps = new Properties();
            cryptoProps.load(lPropStream);
            String lSecret = cryptoProps.getProperty("secret");
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(lSecret.getBytes("UTF-8"));
            byte[] keyBytes = new byte[16];
            System.arraycopy(digest.digest(),0, keyBytes, 0, keyBytes.length);
            return new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
