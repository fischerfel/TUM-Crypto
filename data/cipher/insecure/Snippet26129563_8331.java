public final class CipherUtil {
private static Logger log = Logger.getLogger(CipherUtil.class);

private static final String SECRET_KEY = "XXX";
private static Cipher cipher;
private static SecretKeySpec secretKeySpec;

static{
    try {
        cipher = Cipher.getInstance("AES");
    } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
        log.error(ex);
    }
    byte[] key = null;
    try {
        key = Hex.decodeHex(SECRET_KEY.toCharArray());
    } catch (DecoderException ex) {
        log.error(ex);
    }
    secretKeySpec = new SecretKeySpec(key, "AES");
}

private CipherUtil() { }

public static String encrypt(String plainText) { 
  cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
  ... 
}
public static String decrypt(String encryptedText) { 
  cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
  ...
}
}
