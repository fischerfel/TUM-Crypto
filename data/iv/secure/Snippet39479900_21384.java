@Nullable Cipher getCipher(@NonNull final FingerprintStore ivStore) {
    if (mKeyStore != null && mKeyGenerator != null && mCipher != null) {
        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey)mKeyStore.getKey(KEY_NAME, null);

            switch (mEncryptionMode) {
                case MODE_ENCRYPT:
                    mCipher.init(Cipher.ENCRYPT_MODE, key);
                    ivStore.writeIv(mCipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV());
                    break;
                case MODE_DECRYPT:
                    byte[] iv = ivStore.readIv();
                    mCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
                    break;
            }
            return mCipher;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException
                | InvalidParameterSpecException | NullPointerException e) {
            return null;
        }
    }

    return null;
}
