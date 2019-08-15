public AESCipher(Key key) {
        this(key.getEncoded());
    }

    /**
     * Create AESCipher based on existing {@link Key} and Initial Vector (iv) in bytes
     *
     * @param key Key
     */
    public AESCipher(Key key, byte[] iv) {
        this(key.getEncoded(), iv);
    }

    /**
     * <p>Create AESCipher using a byte[] array as a key</p>
     * <p/>
     * <p><strong>NOTE:</strong> Uses an Initial Vector of 16 0x0 bytes. This should not be used to create strong security.</p>
     *
     * @param key Key
     */
    public AESCipher(byte[] key) {
        this(key, INITIAL_IV);
    }

    private AESCipher(byte[] key, byte[] iv) {
        try {
            this.secretKeySpec = new SecretKeySpec(key, "AES");
            this.iv = new IvParameterSpec(iv);
            this.cipher = Cipher.getInstance(ALGORITHM_AES256);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw Throwables.propagate(e);
        }
    }
