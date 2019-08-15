public KeyStore getKeyStore() {
    try {
        return KeyStore.getInstance("AndroidKeyStore");
    } catch (KeyStoreException exception) {
        throw new RuntimeException("Failed to get an instance of KeyStore", exception);
    }
}

public KeyPairGenerator getKeyPairGenerator() {
    try {
        return KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
    } catch (NoSuchAlgorithmException | NoSuchProviderException exception) {
        throw new RuntimeException("Failed to get an instance of KeyPairGenerator", exception);
    }
}

public Cipher getCipher() {
    try {
        return Cipher.getInstance("RSA");
    } catch (NoSuchAlgorithmException | NoSuchPaddingException exception) {
        throw new RuntimeException("Failed to get an instance of Cipher", exception);
    }
}

private void createKeyPair() {
    try {
        mKeyPairGenerator = getKeyPairGenerator();
        mKeyPairGenerator.initialize(
                new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_DECRYPT)
                        .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                        .setUserAuthenticationRequired(true)
                        .build());
        mKeyPairGenerator.generateKeyPair();
    } catch (InvalidAlgorithmParameterException exception) {
        throw new RuntimeException(exception);
    }
}

private boolean initCipher(int opmode) {
    try {
        mKeyStore = getKeyStore();
        mKeyStore.load(null);

        mCipher = getCipher();

        if (opmode == Cipher.ENCRYPT_MODE) {

            PublicKey key = mKeyStore.getCertificate(KEY_NAME).getPublicKey();

            PublicKey unrestricted = KeyFactory.getInstance(key.getAlgorithm())
                    .generatePublic(new X509EncodedKeySpec(key.getEncoded()));

            OAEPParameterSpec spec = new OAEPParameterSpec(
                    "SHA-256", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
            mCipher.init(opmode, unrestricted, spec);
        } else {
            PrivateKey key = (PrivateKey) mKeyStore.getKey(KEY_NAME, null);
            mCipher.init(opmode, key);
        }

        return true;
    } catch (KeyPermanentlyInvalidatedException exception) {
        return false;
    } catch (KeyStoreException | CertificateException | UnrecoverableKeyException
            | IOException | NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | InvalidAlgorithmParameterException exception) {
        throw new RuntimeException("Failed to initialize Cipher", exception);
    }
}


private void encrypt(String password) {
    try {
        initCipher(Cipher.ENCRYPT_MODE);
        byte[] bytes = mCipher.doFinal(password.getBytes());
        enrcyptedPassword = Base64.encodeToString(bytes, Base64.NO_WRAP);
        Log.d("EncryptedText", enrcyptedPassword);
    } catch (IllegalBlockSizeException | BadPaddingException exception) {
        throw new RuntimeException("Failed to encrypt password", exception);
    }
}

private String decryptPassword(Cipher cipher) {
    try {
        initCipher(Cipher.DECRYPT_MODE);
        byte[] bytes = Base64.decode(enrcyptedPassword, Base64.NO_WRAP);
        return new String(cipher.doFinal(bytes));
    } catch (IllegalBlockSizeException | BadPaddingException | RuntimeException exception) {
        throw new RuntimeException("Failed to decrypt password", exception);
    }
}
