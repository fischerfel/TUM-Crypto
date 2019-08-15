public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String SECRET_KEY = "PloasdlOoasdllasiewjsaroo9o55ooo"; // 256 bits
        // encryptedString is encrypted form of the string "This is just some test data." It has
        // the initialization vector added as a prefix.
        final String encryptedString = "yDdtrKCl30b+fndIMkhasDhBNk+OGYqiM6uVVF89pu2WSnjMsz1lnw6x4H23QWxP";
        String decryptedString;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            decryptedString = doDecryption(encryptedString, SECRET_KEY);
        } catch (Exception e) {
            decryptedString = "Error doing decryption!";
            e.printStackTrace();
        }
        Log.d(TAG, ">>Decrypted string=" + decryptedString);
    }

    /**
     * Decrypt from Base 64 representation of a string with initialization vector prefix.
     *
     * @param sourceBase64 Initialization vector prefixed to string to decrypt in base 64.
     * @param secretKey    The secret key used to do the encryption and the decryption.
     * @return The decrypted string.
     * @throws Exception Exception
     */

    String doDecryption(String sourceBase64, String secretKey) throws Exception {
        Cipher cipher;                  // The cipher used to encrypt and decrypt
        int cipherBlockSize;            // Size of the cipher block
        byte[] sourceBytes;             // Decoded byte array from sourceBase64
        byte[] iv;                      // Initialization vector
        byte[] bytesToDecrypt;          // Bytes on which to perform decryption

        // String passed in is in base 64. Translate so we can work with it.
        sourceBytes = Base64.decode(sourceBase64.getBytes("UTF-8"), Base64.DEFAULT);

        // Get the secretKey spec for our secret key to work with our cipher.
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

        // Set up the cipher.
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipherBlockSize = cipher.getBlockSize();

        // The initialization vector prefixes the string that was passed in (sourceBase64).
        // The length of this initialization vector is the same as our cipher's blocksize.
        iv = new byte[cipherBlockSize];

        // Split the inititializatoin vector from the bytes we need to decrypt.
        bytesToDecrypt = new byte[sourceBytes.length - cipherBlockSize];
        System.arraycopy(sourceBytes, 0, iv, 0, iv.length);
        System.arraycopy(sourceBytes, iv.length, bytesToDecrypt, 0, bytesToDecrypt.length);

        // Now do the actual decryption.
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
        return new String(cipher.doFinal(bytesToDecrypt));
    }

    private static final String TAG = "MainActivity";
}
