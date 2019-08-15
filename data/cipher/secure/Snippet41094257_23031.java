@TargetApi(Build.VERSION_CODES.M)
public KeyStoreHelper(boolean encrypt) {
    try {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
        if (encrypt) {
            keyPairGenerator.initialize(
                    new KeyGenParameterSpec.Builder(
                            MY_KEY_NAME_INSIDE_KEYSTORE,
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                            .build());
        } else {
            keyPairGenerator.initialize(
                    new KeyGenParameterSpec.Builder(
                            MY_KEY_NAME_INSIDE_KEYSTORE,
                            KeyProperties.PURPOSE_DECRYPT)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                            .build());
        }
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        String provider = Build.VERSION.SDK_INT < Build.VERSION_CODES.M ? "AndroidOpenSSL" : "AndroidKeyStoreBCWorkaround";

        if (encrypt) {
            PublicKey publicKey = keyPair.getPublic();
            mInCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
            mInCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        } else {
            PrivateKey privateKey = keyPair.getPrivate();
            mOutCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
            mOutCipher.init(Cipher.DECRYPT_MODE, privateKey);
        }
    } catch (Exception e) {
        Log.e(ERROR_TAG, Log.getStackTraceString(e));
    }
}
public static KeyStoreHelper getInstance(boolean encrypt) {
    if (mKeyStoreHelperInstance == null) {
        mKeyStoreHelperInstance = new KeyStoreHelper(encrypt);
    }
    return mKeyStoreHelperInstance;
}
