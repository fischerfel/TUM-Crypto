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

// generateKey(char[] passphraseOrPin, byte[] salt) remains the same
