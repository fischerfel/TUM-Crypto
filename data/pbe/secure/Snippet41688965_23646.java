byte[] a = encryptFIN128AES("pls");
String b = decryptFIN128AES(a);
Log.e("AES_Test", "b = " + b);

/**
     * Encrypts a string with AES (128 bit key)
     * @param fin
     * @return the AES encrypted string
     */
    private byte[] encryptFIN128AES(String fin) {

        SecretKeySpec sks = null;

        try {
            sks = new SecretKeySpec(generateKey(PASSPHRASE, SALT.getBytes(StandardCharsets.UTF_8)).getEncoded(), "AES");
        } catch (Exception e) {
            Log.e("encryptFIN128AES", "AES key generation error");
        }

        // Encode the original data with AES
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, sks);
            encodedBytes = c.doFinal(fin.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            Log.e("encryptFIN128AES", "AES encryption error");
        }

        return encodedBytes;

    }


    /**
     * Decrypts a string with AES (128 bit key)
     * @param encodedBytes
     * @return the decrypted String
     */
    private String decryptFIN128AES(byte[] encodedBytes) {

        SecretKeySpec sks = null;

        try {
            sks = new SecretKeySpec(generateKey(PASSPHRASE, SALT.getBytes(StandardCharsets.UTF_8)).getEncoded(), "AES");
        } catch (Exception e) {
            Log.e("decryptFIN128AES", "AES key generation error");
        }

        // Decode the encoded data with AES
        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, sks);
            decodedBytes = c.doFinal(encodedBytes);
        } catch (Exception e) {
            Log.e("decryptFIN128AES", "AES decryption error");
        }

        //return Base64.encodeToString(decodedBytes, Base64.DEFAULT);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }


/**
     * Build private key from a passpharase/PIN (incl. key derivation (Uses PBKDF2))
     * @param passphraseOrPin
     * @param salt
     * @return The generated SecretKey (Used for AES-encryption, key size specified in outputKeyLength)
     */
    public static SecretKey generateKey(char[] passphraseOrPin, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Number of PBKDF2 hardening rounds to use. Larger values increase
        // computation time. You should select a value that causes computation
        // to take >100ms.
        final int iterations = 1000;

        // Generate a 256-bit key
        final int outputKeyLength = 128;

        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations, outputKeyLength);
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        return secretKey;
    }
