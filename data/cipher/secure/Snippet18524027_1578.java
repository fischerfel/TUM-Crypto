public class LockManagerTest {
    // Need to share the IV and key between encode and decode
    private static byte[] aesInitialisationVector;
    private static SecretKey aesSecretKey;
    private static Cipher aesCipher;

    public LockManagerTest(String sessionKey) {
        try {
            byte[] key = getSecretKey(sessionKey.toCharArray(), getSalt(32),
                                      65536, 128);
            aesSecretKey = new SecretKeySpec(key, "AES");
            aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesCipher.init(Cipher.ENCRYPT_MODE, aesSecretKey);
            AlgorithmParameters params = aesCipher.getParameters();
            aesInitialisationVector =
                    params.getParameterSpec(IvParameterSpec.class).getIV();
        } catch (Exception e) {
            Util.handleException(e);
        }
    }

    private static byte[] getSecretKey(char[] plaintext,
                                       byte[] salt,
                                       int iterations,
                                       int keySize)
            throws Exception {
        PBEKeySpec spec = new PBEKeySpec(plaintext, salt, iterations, keySize);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return skf.generateSecret(spec).getEncoded();
    }

    private static byte[] getSalt(int keyLength) throws Exception {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        byte[] salt = new byte[keyLength];
        random.nextBytes(salt);
        return salt;
    }

    public byte[] encryptedAes(char[] input) throws Exception {
        // WRONG
        // aesCipher.init(Cipher.ENCRYPT_MODE, aesSecretKey);
        //
        aesCipher.init(Cipher.ENCRYPT_MODE, aesSecretKey, 
                       new IvParameterSpec(aesInitialisationVector);
        CharBuffer cBuf = CharBuffer.wrap(input);
        byte[] normalised = Charset.forName("UTF-8").encode(cBuf).array();
        byte[] ciphertext = aesCipher.doFinal(normalised);
        return ciphertext;
    }

    public byte[] decryptAes(byte[] ciphertext) throws Exception {
        aesCipher.init(Cipher.DECRYPT_MODE,
                aesSecretKey, new IvParameterSpec(aesInitialisationVector));
        byte[] plaintext = aesCipher.doFinal(ciphertext);
        return plaintext;
    }
}
