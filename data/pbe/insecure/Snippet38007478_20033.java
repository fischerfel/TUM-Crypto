public class PBE extends AppCompatActivity {

    private static final String salt = "A long, but constant phrase that will be used each time as the salt.";
    private static final int iterations = 2000;
    private static final int keyLength = 256;
    private static final SecureRandom random = new SecureRandom();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pbe);

        try {
            Security.insertProviderAt(new BouncyCastleProvider(), 1);
            //Security.addProvider(new BouncyCastleProvider());

            String passphrase = "The quick brown fox jumped over the lazy brown dog";
            String plaintext = "Hello";
            byte [] ciphertext = encrypt(passphrase, plaintext);
            String recoveredPlaintext = decrypt(passphrase, ciphertext);

            TextView decryptedTv = (TextView) findViewById(R.id.tv_decrypt);

            decryptedTv.setText(recoveredPlaintext);

            System.out.println(recoveredPlaintext);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static byte [] encrypt(String passphrase, String plaintext) throws Exception {
        SecretKey key = generateKey(passphrase);

        Cipher cipher = Cipher.getInstance("AES/CTR/NOPADDING");//,new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, key, generateIV(cipher), random);
        return cipher.doFinal(plaintext.getBytes());
    }

    private static String decrypt(String passphrase, byte [] ciphertext) throws Exception {
        SecretKey key = generateKey(passphrase);

        Cipher cipher = Cipher.getInstance("AES/CTR/NOPADDING");// , new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, key, generateIV(cipher), random);
        return new String(cipher.doFinal(ciphertext));
    }

    private static SecretKey generateKey(String passphrase) throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt.getBytes(), iterations, keyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWITHSHA256AND256BITAES-CBC-BC");
        return keyFactory.generateSecret(keySpec);
    }

    private static IvParameterSpec generateIV(Cipher cipher) throws Exception {
        byte [] ivBytes = new byte[cipher.getBlockSize()];
        random.nextBytes(ivBytes);
        return new IvParameterSpec(ivBytes);
    }

}
