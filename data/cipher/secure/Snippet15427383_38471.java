public class Main extends Activity {

    // Decrypt tester

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String plainText = "hello";
        // Base64 Encoded
        String encryptedText = "SWfL6wpS87SsvByIfLO1W5Gg/gOMqtplNtqBrDELfQav0ipCy0BddHpTlmlgQO+6HRY2JWsw6hp5rlmmBJG722Ujc4Rm5MZr8BbKWMgX89rvmvUG/YeswTjHZsrzbx6k6krChM55dsJCU3JSXPMIcTTgRxVValgLeCfX1SwoOpNZ2m4WQAhX6NrSYwSXGLUHfrguQogTpU5wEcDQ0HR4vBlQO5fU8z2MwoP50Vs8f+4EysEwSy9ezsYHWDtW8M9t3EuF8Roq++/Y8GLASrYZxuAA0IPHGvR9Qufa04i7HlAAsDu9LQuOGUf35KkpFoCx0XyuA0qppccGdqHuK5IIoQ==";
        try {
            String encrypted = encrypt(getAssets().open("public_key.der"), plainText);
            Log.d("Encrypted", encrypted);
            // Returns a base64 encoded string.

            Log.d("TAG", "Decryption start");

            String decrypted = decrypt(getAssets().open("public_key.der"), encryptedText);
            Log.d("Decrypted", decrypted);
        } catch (Exception e) {
            Log.e("TEST", Log.getStackTraceString(e));
        }
    }

    public static String encrypt(InputStream stream, String plaintext) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        byte[] encodedKey = new byte[stream.available()];
        stream.read(encodedKey);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pkPublic = kf.generatePublic(publicKeySpec);

        Cipher pkCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        pkCipher.init(Cipher.ENCRYPT_MODE, pkPublic);
        byte[] encryptedInByte = pkCipher.doFinal(plaintext.getBytes());

        String encryptedInString = new String(Base64Coder.encode(encryptedInByte));

        return encryptedInString;
    }

    public static String decrypt(InputStream stream, String encrypted) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        byte[] encodedKey = new byte[stream.available()];
        stream.read(encodedKey);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pkPublic = kf.generatePublic(publicKeySpec);

        Cipher pkCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        pkCipher.init(Cipher.DECRYPT_MODE, pkPublic);
        byte[] decryptedInByte = pkCipher.doFinal(encrypted.getBytes());
        String decryptedInString = new String(Base64Coder.encode(decryptedInByte));
        return decryptedInString;
    }
}
