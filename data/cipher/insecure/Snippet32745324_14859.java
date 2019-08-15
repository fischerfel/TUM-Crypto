public class MainActivity extends AppCompatActivity {

static byte[] salt = {  (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
                                (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        decryptZipFile();
    }


    private byte[] getKey() {
        try {
            //Create the key for the encryption/decryption
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(salt);
            kgen.init(128, sr); // 192 and 256 bits may not be available
            SecretKey skey = kgen.generateKey();
            byte[] key = skey.getEncoded();
            return key;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static byte[] decrypt(byte[] key, byte[] encryptedData) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key,"AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encryptedData);
        return decrypted;
    }


    public void decryptZipFile() {
        // First decrypt the zip file
        try {
            InputStream is = getResources().getAssets().open("pics.zip.encrypted");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int count;
            while ((count = is.read(buffer)) != -1)
                baos.write(buffer, 0, count);
            byte[] encryptedData = baos.toByteArray();

            byte[] decryptedData = decrypt(getKey(), encryptedData);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
