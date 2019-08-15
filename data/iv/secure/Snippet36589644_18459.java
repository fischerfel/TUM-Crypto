    private static final String TAG = Crypto.class.getSimpleName();

    private static final String CIPHER_ALGORITHM = "AES/CBC/NoPadding";

    private static int KEY_LENGTH = 32;
    private static int KEY_LENGTH_SHORT = 16;
    // minimum values recommended by PKCS#5, increase as necessary
    private static int ITERATION_COUNT = 1000;
    // Should generate random salt for each repo
    private static byte[] salt = {(byte) 0xda, (byte) 0x90, (byte) 0x45, (byte) 0xc3, (byte) 0x06, (byte) 0xc7, (byte) 0xcc, (byte) 0x26};

    private Crypto() {
    }

    /**
     * decrypt repo encKey
     *
     * @param password
     * @param randomKey
     * @param version
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static String deriveKeyPbkdf2(String password, String randomKey, int version) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(randomKey)) {
            return null;
        }

        PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA256Digest());
        gen.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(password.toCharArray()), salt, ITERATION_COUNT);
        byte[] keyBytes;

        if (version == 2) {
            keyBytes = ((KeyParameter) gen.generateDerivedMacParameters(KEY_LENGTH * 8)).getKey();
        } else
            keyBytes = ((KeyParameter) gen.generateDerivedMacParameters(KEY_LENGTH_SHORT * 8)).getKey();

        SecretKey realKey = new SecretKeySpec(keyBytes, "AES");

        final byte[] iv = deriveIVPbkdf2(realKey.getEncoded());

        return seafileDecrypt(fromHex(randomKey), realKey, iv);
    }

    public static byte[] deriveIVPbkdf2(byte[] key) throws UnsupportedEncodingException {
        PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA256Digest());
        gen.init(key, salt, 10);
        return ((KeyParameter) gen.generateDerivedMacParameters(KEY_LENGTH_SHORT * 8)).getKey();
    }

    /**
     * All file data is encrypted by the file key with AES 256/CBC.
     *
     * We use PBKDF2 algorithm (1000 iterations of SHA256) to derive key/iv pair from the file key.
     * After encryption, the data is uploaded to the server.
     *
     * @param plaintext
     * @param key
     * @return
     */
    private static byte[] seafileEncrypt(byte[] plaintext, SecretKey key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);

            return cipher.doFinal(plaintext);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e(TAG, "NoSuchAlgorithmException " + e.getMessage());
            return null;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            Log.e(TAG, "InvalidKeyException " + e.getMessage());
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            Log.e(TAG, "InvalidAlgorithmParameterException " + e.getMessage());
            return null;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            Log.e(TAG, "NoSuchPaddingException " + e.getMessage());
            return null;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            Log.e(TAG, "IllegalBlockSizeException " + e.getMessage());
            return null;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            Log.e(TAG, "BadPaddingException " + e.getMessage());
            return null;
        }
    }

    /**
     * All file data is encrypted by the file key with AES 256/CBC.
     *
     * We use PBKDF2 algorithm (1000 iterations of SHA256) to derive key/iv pair from the file key.
     * After encryption, the data is uploaded to the server.
     *
     * @param plaintext
     * @param key
     * @return
     */
    public static byte[] encrypt(byte[] plaintext, String key, byte[] iv, int version) throws NoSuchAlgorithmException {
        PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA256Digest());
        gen.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(key.toCharArray()), salt, ITERATION_COUNT);
        byte[] keyBytes;

        if (version == 2) {
            keyBytes = ((KeyParameter) gen.generateDerivedMacParameters(KEY_LENGTH * 8)).getKey();
        } else
            keyBytes = ((KeyParameter) gen.generateDerivedMacParameters(KEY_LENGTH_SHORT * 8)).getKey();

        SecretKey realKey = new SecretKeySpec(keyBytes, "AES");
        return seafileEncrypt(plaintext, realKey , iv);
    }
