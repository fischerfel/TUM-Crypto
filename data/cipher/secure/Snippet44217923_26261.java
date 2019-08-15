public static byte[] encrypt_cbc(SecretKey skey, String plaintext) {
        /* Precond: skey is valid; otherwise IllegalStateException will be thrown. */
        try {
            byte[] ciphertext = null;
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");           
            final int blockSize = cipher.getBlockSize();
            byte[] initVector = new byte[blockSize];
            (new SecureRandom()).nextBytes(initVector);
            IvParameterSpec ivSpec = new IvParameterSpec(initVector);
            cipher.init(Cipher.ENCRYPT_MODE, skey, ivSpec);
            byte[] encoded = plaintext.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            ciphertext = new byte[initVector.length + cipher.getOutputSize(encoded.length)];
            for (int i=0; i < initVector.length; i++) {
                ciphertext[i] = initVector[i];
            }
            // Perform encryption
            cipher.doFinal(encoded, 0, encoded.length, ciphertext, initVector.length);
            return ciphertext;
        } catch (NoSuchPaddingException | InvalidAlgorithmParameterException | ShortBufferException |
            BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException e)
        {
            /* None of these exceptions should be possible if precond is met. */
            throw new IllegalStateException(e.toString());
        }
    }

    public static String decrypt_cbc(SecretKey skey, byte[] ciphertext)
        throws BadPaddingException, IllegalBlockSizeException /* these indicate corrupt or malicious ciphertext */
    {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");           
            final int blockSize = cipher.getBlockSize();
            byte[] initVector = Arrays.copyOfRange(ciphertext, 0, blockSize);
            IvParameterSpec ivSpec = new IvParameterSpec(initVector);
            cipher.init(Cipher.DECRYPT_MODE, skey, ivSpec);
            byte[] plaintext = cipher.doFinal(ciphertext, blockSize, ciphertext.length - blockSize);
            return new String(plaintext);
        } catch (NoSuchPaddingException | InvalidAlgorithmParameterException |
            InvalidKeyException | NoSuchAlgorithmException e)
        {
            /* None of these exceptions should be possible if precond is met. */
            throw new IllegalStateException(e.toString());
        }
    }
