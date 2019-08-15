private static byte[] passwordDecrypt(char[] password, byte[] ciphertext) {
    byte[] salt = new byte[8];
    ByteArrayInputStream bais = new ByteArrayInputStream(ciphertext);
    bais.read(salt, 0, 8);

    byte[] remainingCiphertext = new byte[ciphertext.length - 8];
    bais.read(remainingCiphertext, 0, ciphertext.length - 8);


    PBEKeySpec keySpec = new PBEKeySpec(password, salt, 1000, 64);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndTripleDES");
    SecretKey key = keyFactory.generateSecret(keySpec);
    PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATIONS);
    Cipher cipher = Cipher.getInstance("PBEWithMD5AndTripleDES");

    // Perform decryption.
    cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

    return cipher.doFinal(remainingCiphertext);
}

private static byte[] passwordEncrypt(char[] password, byte[] plaintext) {
    byte[] salt = new byte[8];
    Random random = new Random();
    random.nextBytes(salt);

    PBEKeySpec keySpec = new PBEKeySpec(password, salt, 1000, 64);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndTripleDES");
    SecretKey key = keyFactory.generateSecret(keySpec);
    PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATIONS);
    Cipher cipher = Cipher.getInstance("PBEWithMD5AndTripleDES");
    cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

    // Encrypt array
    byte[] ciphertext = cipher.doFinal(plaintext);

    // Write out the salt, then the ciphertext and return it.
    ByteArrayOutputStream b = new ByteArrayOutputStream();
    b.write(salt);
    b.write(ciphertext);
    return b.toByteArray();
}
