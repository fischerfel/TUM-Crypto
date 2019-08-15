private static final String fallbackSalt = "ajefa6tc73t6raiw7tr63wi3r7citrawcirtcdg78o2vawri7t";
private static final int iterations = 2000;
private static final int keyLength = 256;
private static final SecureRandom random = new SecureRandom();

public byte[] encrypt(String plaintext, String passphrase, String salt)
        throws Exception {
    SecretKey key = generateKey(passphrase, salt);
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
    cipher.init(Cipher.ENCRYPT_MODE, key, generateIV(cipher),random);
    return cipher.doFinal(plaintext.getBytes());
}

public String decrypt(byte[] encrypted, String passphrase, String salt)
        throws Exception {
    SecretKey key = generateKey(passphrase, salt);
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
    cipher.init(Cipher.DECRYPT_MODE, key, generateIV(cipher),random);
    return new String(cipher.doFinal(encrypted));
}

private SecretKey generateKey(String passphrase, String salt)
        throws Exception {
    PBEKeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(),
            salt.getBytes(), iterations, keyLength);
    SecretKeyFactory keyFactory = SecretKeyFactory
            .getInstance("PBEWITHSHA256AND256BITAES-CBC-BC");
    return keyFactory.generateSecret(keySpec);
}

private IvParameterSpec generateIV(Cipher cipher) throws Exception {
    byte[] ivBytes = new byte[cipher.getBlockSize()];
    random.nextBytes(ivBytes);
    return new IvParameterSpec(ivBytes);
}
