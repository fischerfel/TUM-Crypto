private static final String ALGORITHM_TYPE = "AES";
private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
private static byte[] INITIALIZATION_VECTOR = new byte[] {
    0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
    0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00
};
public String encrypt(String token) {
    Cipher cipher = null;
    SecretKey key = null;
    String tokenAsHex = null;
    byte[] encryptedToken = null;
    byte[] sksKey = getKeyAsByteArray(KEY); // SecretKeySpec key.

    try {
        key = new SecretKeySpec(sksKey, ALGORITHM_TYPE);
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(INITIALIZATION_VECTOR);
        cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        encryptedToken = cipher.doFinal(Base64.encodeBase64(token.getBytes("UTF-8")));
    } catch (Exception e) {
        throw new EncryptionException(e);
    }
    return Base64.encodeBase64String(encryptedToken).toLowerCase();
}

public String decrypt(String token) throws EncryptionException {
    Cipher cipher = null;
    SecretKey key = null;
    byte[] decryptedToken = null;
    byte[] sksKey = getKeyAsByteArray(KEY); // SecretKeySpec key.
    try {
        key = new SecretKeySpec(sksKey, ALGORITHM_TYPE);            
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(INITIALIZATION_VECTOR);
        cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        decryptedToken = cipher.doFinal(Base64.decodeBase64(token));
    } catch(Exception e){
        throw new EncryptionException(e);    
    }
    if (decryptedToken == null) {
         throw new EncryptionException("Unable to decrypt the following token: " + token);
    }
    return Base64.encodeBase64String(decryptedToken);
}
