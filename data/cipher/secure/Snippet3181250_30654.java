public static void main(String... args) throws Exception {
    byte[] data = "hello".getBytes();

    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(128); // 192 and 256 bits may not be available

    SecretKey secretKey = keyGenerator.generateKey();

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    // By initializing the cipher in CBC mode, an "initialization vector" has been randomly
    // generated. This initialization vector will be necessary to decrypt the encrypted data.
    // It is safe to store the initialization vector in plain text for later use. You can obtain
    // it's bytes by calling iv.getIV().
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    IvParameterSpec iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class);
    byte[] encryptedData = cipher.doFinal(data);

    // When decrypting the encrypted data, you must provide the initialization vector used
    // during the encryption phase.
    cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
    byte[] decryptedData = cipher.doFinal(encryptedData);

    if (!Arrays.equals(data, decryptedData)) {
        throw new Exception("Data was not decrypted successfully");
    }
}
