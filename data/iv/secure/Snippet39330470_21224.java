public boolean initCipher(int opMode) {
    try {
        Key key = mKeyStore.getKey(KEY_NAME, null);

        if (opMode == Cipher.ENCRYPT_MODE) {
            final byte[] encoded = key.getEncoded();
            final String algorithm = key.getAlgorithm();
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            PublicKey unrestricted = KeyFactory.getInstance(algorithm).generatePublic(keySpec);

            mCipher.init(opMode, unrestricted);

        } else {
            final IvParameterSpec ivParameterSpec = getIvParameterSpec();
            mCipher.init(opMode, key, ivParameterSpec);

        }

        return true;

    } catch (KeyPermanentlyInvalidatedException exception) {
        return false;

    } catch ( NoSuchAlgorithmException | InvalidKeyException
            | InvalidKeySpecException | InvalidAlgorithmParameterException | UnrecoverableKeyException | KeyStoreException exception) {
        throw new RuntimeException("Failed to initialize Cipher or Key: ", exception);
    }
}

@NonNull
public IvParameterSpec getIvParameterSpec() {
    // the IV is stored in the Preferences after encoding.
    String base64EncryptionIv = PreferenceHelper.getEncryptionIv(mContext);
    byte[] encryptionIv = Base64.decode(base64EncryptionIv, Base64.DEFAULT);
    return new IvParameterSpec(encryptionIv);
}
