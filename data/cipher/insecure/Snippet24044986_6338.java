private static final String KEY_FACTORY_ALGORITHM = "PBEWITHSHAAND128BITAES-CBC-BC";
private static final String KEY_ALGORITHM = "AES";
private static final String CIPHER_PROVIDER = "AES";

public Crypto(String password, byte[] salt) {

    if (TextUtils.isEmpty(password)) {
        throw new IllegalArgumentException(
                "password cannot be null or empty.");
    }

    PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 20,
            128);

    SecretKeyFactory factory;
    try {
        factory = SecretKeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
        PBEKey key = (PBEKey) factory.generateSecret(keySpec);

        this.createKeyAndCipher(key.getEncoded());
    } catch (Exception e) {
        Log.e(this.getClass().getName(), Log.getStackTraceString(e));
        throw new RuntimeException(e);
    }
}

private void createKeyAndCipher(byte[] keyData)
        throws NoSuchAlgorithmException, NoSuchPaddingException {
    _secretKey = new SecretKeySpec(keyData, KEY_ALGORITHM);
    _cipher = Cipher.getInstance(CIPHER_PROVIDER);
}
