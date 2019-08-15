public class TrippleDESEncryption {
   private static final String UNICODE_FORMAT = "UTF8";
   public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
   private KeySpec keySpec;
   private SecretKeyFactory secretKeyFactory;
   private Cipher cipher;
   byte[] keyAsBytes;
   private String encryptionKey;
   private String encryptionScheme;
   SecretKey key;

   public TrippleDESEncryption() throws Exception {
          encryptionKey = "234342343423434234342343";
          encryptionScheme = DESEDE_ENCRYPTION_SCHEME;
          keyAsBytes = encryptionKey.getBytes(UNICODE_FORMAT);
          keySpec = new DESedeKeySpec(keyAsBytes);
          secretKeyFactory = SecretKeyFactory.getInstance(encryptionScheme);
          cipher = Cipher.getInstance(encryptionScheme);
          key = secretKeyFactory.generateSecret(keySpec);

   }

   /**
   * Method To Encrypt The String
   */
   public String encrypt(String unencryptedString) {
          String encryptedString = null;
          try {
                 cipher.init(Cipher.ENCRYPT_MODE, key);
                 byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
                 byte[] encryptedText = cipher.doFinal(plainText);
                 BASE64Encoder base64encoder = new BASE64Encoder();
                 encryptedString = base64encoder.encode(encryptedText);
          } catch (Exception e) {
                 e.printStackTrace();
          }
          return encryptedString;
    }
}
