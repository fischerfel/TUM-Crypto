private Cipher encryptCipher = null;
private Cipher decryptCipher = null;

static {
    Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
}

public void initialize(SecretKey key) throws Exception {
    try {
        encryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding", "SC");
        decryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding", "SC");
    } catch (Exception e) {
    }

    encryptCipher.init(Cipher.ENCRYPT_MODE, key);
    decryptCipher.init(Cipher.DECRYPT_MODE, key);
}

public String encryptBase64(String unencryptedString) throws Exception {
    byte[] unencryptedByteArray = unencryptedString.getBytes("UTF-8");
    byte[] encryptedBytes = encryptCipher.doFinal(unencryptedByteArray);
    return new String(Base64.encodeBase64(encryptedBytes), "UTF-8");
}

public String decryptBase64(String encryptedString) throws Exception {
    try {
        byte[] unencryptedByteArray = decryptCipher.doFinal(Base64.decodeBase64(encryptedString.getBytes("UTF-8")));
        String message = new String(unencryptedByteArray, "UTF-8");
        return message;
    } catch (Exception e) {
    }

    return null;
}
