public class NewCipher {

    private static final String password = "somestatickey";
    private Cipher desCipher;
    private SecretKey secretKey;
    private Context ctx;

    public NewCipher(Context ctx) throws Exception {

        this.ctx = ctx;
        // Create Key
        byte key[] = password.getBytes();
        DESKeySpec desKeySpec = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        secretKey = keyFactory.generateSecret(desKeySpec);

        // Create Cipher
        desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

    }
