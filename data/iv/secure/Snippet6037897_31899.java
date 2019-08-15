    public static final String FILE_EXTENSION = ".serpent";
    public static final String PROVIDER = "BC"; // Bouncy Castle
    public static final String BLOCK_CIPHER = "Serpent";
    public static final String TRANSFORMATION = "Serpent/CBC/PKCS7Padding";
    public static final String KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
    public static final String PRNG_ALGORITHM = "SHA1PRNG";

    public static final int BLOCK_SIZE = 128; // bits
    public static final int KEY_SIZE = 256; // bits
    public static final int ITERATION_COUNT = 1024; // for PBE

    /** Performs the encryption of a file. */
    public void encrypt(String pathname, char[] password, byte[] salt) {
        // Use bouncy castle as our provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // Convert the file into a byte array
        byte[] plaintext = fileToBytes(new File(pathname));

        // Generate a 256-bit key
        SecretKey key = generateSecretKey(password,salt);

        // Generate a 128-bit secure random IV
        byte[] iv = generateIV();

        // Setup the cipher and perform the encryption
        Cipher cipher = null;
        byte[] ciphertext = null;
        try {
            cipher = Cipher.getInstance(TRANSFORMATION, PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            ciphertext = cipher.doFinal(plaintext);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Append the IV to the ciphertext
        byte[] temp = new byte[iv.length+ciphertext.length];
        System.arraycopy(iv, 0, temp, 0, iv.length);
        System.arraycopy(ciphertext, 0, temp, iv.length, ciphertext.length);
        ciphertext = temp;

        // Store the encrypted file in the same directory we found it
        if (Environment.getExternalStorageDirectory().canWrite()) {
            bytesToFile(ciphertext, pathname+FILE_EXTENSION);
            File file = new File(pathname);
            file.delete();
        }       
    }
