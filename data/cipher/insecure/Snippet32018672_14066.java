public class CryptUtil {
    public static final String DEFAULT_KEY = "0123456789";

    private static CryptUtil instance;

    private String chiperKey;

    private CryptUtil(String chiperKey) {
        this.chiperKey = chiperKey;
    }

    public static CryptUtil getInstance() {
        if (null == instance) {
            instance = new CryptUtil(DEFAULT_KEY);
        }

        return instance;
    }

    public static CryptUtil getInstance(String cipherkey) {
        instance = new CryptUtil(cipherkey);
        return instance;
    }

    public String aesEncrypt(String plainText) {
            byte[] keyBytes = Arrays.copyOf(this.chiperKey.getBytes("ASCII"), 16);

            SecretKey key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] cleartext = plainText.getBytes("UTF-8");
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            final char[] encodeHex = Hex.encodeHex(ciphertextBytes);

            return new String(encodeHex);

        return null;
    }

    public static void main(String[] args) {

        CryptUtil cryptUtil = CryptUtil.getInstance();
        System.out.println(cryptUtil.aesEncrypt("lun01"));
    }
}
