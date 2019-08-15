public class DESCodec {

    /**
     * Secret key that shall be used for encryption and decryption.
     */
    private String strSecretKey = "12345678";

    private static final String UNICODE_FORMAT = "UTF-8";

    private static final String DES_ENCRYPTION_SCHEME = "DES";

    private static final String TAG = "DESCodec";

    private Cipher cipher;

    private SecretKey key;


    public DESCodec() {
        try {
            this.strSecretKey = strSecretKey;
            String myEncryptionScheme = DES_ENCRYPTION_SCHEME;
            byte[] keyAsBytes = strSecretKey.getBytes(UNICODE_FORMAT);
            DESKeySpec myKeySpec = new DESKeySpec(keyAsBytes);
            SecretKeyFactory mySecretKeyFactory = SecretKeyFactory.getInstance(myEncryptionScheme);
            cipher = Cipher.getInstance(myEncryptionScheme);
            key = mySecretKeyFactory.generateSecret(myKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public String desEncrypt(String message) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = message.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);

            encryptedString = Base64.encodeToString(encryptedText, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

    public String desDecrypt(String message) {
        String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decode(message, Base64.DEFAULT);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText = bytes2String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

    private String bytes2String(byte[] bytes) {
        try {
            return new String(bytes, UNICODE_FORMAT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
