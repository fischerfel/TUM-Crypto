public KeyStore getKeyStore() {
    try {
        return KeyStore.getInstance("AndroidKeyStore");
    } catch (KeyStoreException exception) {
        throw new RuntimeException("Failed to get an instance of KeyStore", exception);
    }
}

public KeyPairGenerator getKeyPairGenerator() {
    try {
        return KeyPairGenerator.getInstance("EC", "AndroidKeyStore");
    } catch(NoSuchAlgorithmException | NoSuchProviderException exception) {
        throw new RuntimeException("Failed to get an instance of KeyPairGenerator", exception);
    }
}

public Cipher getCipher() {
    try {
        return Cipher.getInstance("EC");
    } catch(NoSuchAlgorithmException | NoSuchPaddingException exception) {
        throw new RuntimeException("Failed to get an instance of Cipher", exception);
    }
}

private void createKey() {
    try {
        mKeyPairGenerator.initialize(
                new KeyGenParameterSpec.Builder(KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1")
                        .setUserAuthenticationRequired(true)
                        .build());
        mKeyPairGenerator.generateKeyPair();
    } catch(InvalidAlgorithmParameterException exception) {
        throw new RuntimeException(exception);
    }
}

private boolean initCipher(int opmode) {
    try {
        mKeyStore.load(null);

        if(opmode == Cipher.ENCRYPT_MODE) {
            PublicKey key = mKeyStore.getCertificate(KEY_ALIAS).getPublicKey();
            mCipher.init(opmode, key);
        } else {
            PrivateKey key = (PrivateKey) mKeyStore.getKey(KEY_ALIAS, null);
            mCipher.init(opmode, key);
        }

        return true;
    } catch (KeyPermanentlyInvalidatedException exception) {
        return false;
    } catch(KeyStoreException | CertificateException | UnrecoverableKeyException
            | IOException | NoSuchAlgorithmException | InvalidKeyException
            | InvalidAlgorithmParameterException exception) {
        throw new RuntimeException("Failed to initialize Cipher", exception);
    }
}

private void encrypt(String password) {
    try {
        initCipher(Cipher.ENCRYPT_MODE);
        byte[] bytes = mCipher.doFinal(password.getBytes());
        String encryptedPassword = Base64.encodeToString(bytes, Base64.NO_WRAP);
        mPreferences.getString("password").set(encryptedPassword);
    } catch(IllegalBlockSizeException | BadPaddingException exception) {
        throw new RuntimeException("Failed to encrypt password", exception);
    }
}

private String decryptPassword(Cipher cipher) {
    try {
        String encryptedPassword = mPreferences.getString("password").get();
        byte[] bytes = Base64.decode(encryptedPassword, Base64.NO_WRAP);
        return new String(cipher.doFinal(bytes));
    } catch (IllegalBlockSizeException | BadPaddingException exception) {
        throw new RuntimeException("Failed to decrypt password", exception);
    }
}
