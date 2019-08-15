static {
    // This really should be done just once.
    // Moreover, you most probably don't need it.
    Security.addProvider(new BouncyCastleProvider());
}

Encryptor() {
    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding"); 
    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IVBytes));
}

public synchronized String encrypt(String toEncrypt) throws Exception {
    byte[] encrypted = cipher.doFinal(toEncrypt.getBytes("UTF-8"));
    byte[] encryptedValue = Base64.encodeBase64(encrypted);
    return new String(encryptedValue);
}
