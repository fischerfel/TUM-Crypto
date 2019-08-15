public class ParameterEncryptor {

    private static final String ALGO = "AES"; 
    private static Logger log = Logger.getLogger(UrlEncryptedParameterTag.class);
    private static byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't','S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };

    public static String encrypt(String name, String value) 
    { 
        log.debug("Encrypting request parameter: " + name); 
        Key key = generateKey();
        Cipher c =null;
        try {
            c = Cipher.getInstance(ALGO);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(value.getBytes());
            value = new BASE64Encoder().encode(encVal);
        } catch (NoSuchAlgorithmException ex) {
            log.error(ex.getMessage());
        } catch (NoSuchPaddingException ex) {
            log.error(ex.getMessage());
        } catch (InvalidKeyException ex) {
          log.error(ex.getMessage());
        } catch (IllegalBlockSizeException ex) {
            log.error(ex.getMessage());
        } catch (BadPaddingException ex) {
            log.error(ex.getMessage());
        }

        return value;
    } 

    public static String decrypt(String encryptedData) {
        String decryptedValue = "";
        try{
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
            byte[] decValue = c.doFinal(decordedValue);
            decryptedValue = new String(decValue);
        } catch (NoSuchAlgorithmException ex) {
            log.error(ex.getMessage());
        } catch (NoSuchPaddingException ex) {
            log.error(ex.getMessage());
        } catch (InvalidKeyException ex) {
            log.error(ex.getMessage());
        } catch (IllegalBlockSizeException ex) {
            log.error(ex.getMessage());
        } catch (BadPaddingException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return decryptedValue;
    }

    private static Key generateKey() {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }
}
