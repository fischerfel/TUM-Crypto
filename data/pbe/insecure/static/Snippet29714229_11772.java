public class SecurityHelper {
    private static final String DEFAULT_KEY = "some-key-here";
    private SecretKey secretKey;
    private PBEParameterSpec parameterSpec;
    private Cipher cipher;

    public SecurityHelper() {
        try {
            char[] moduleKeyChars = DEFAULT_KEY.toCharArray();
            KeySpec keySpec = new PBEKeySpec(moduleKeyChars);
            secretKey = SecretKeyFactory.getInstance(
              "PBEWithMD5AndDES").generateSecret(keySpec);
            parameterSpec = new PBEParameterSpec(new byte[8], 1);
            cipher = Cipher.getInstance("PBEWithMD5AndDES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String secret) {
        String encrypted = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            byte[] stateBytes = cipher.doFinal(secret.getBytes("UTF-8"));
            encrypted = DatatypeConverter.printBase64Binary(stateBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    public String decrypt(String encrypted) {
        String decrypted = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            byte[] stringBinary = DatatypeConverter.parseBase64Binary(encrypted);
            decrypted = new String(cipher.doFinal(stringBinary));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decrypted;
    }

}
