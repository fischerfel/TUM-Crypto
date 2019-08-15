public static byte[] encrypt(byte[] data, String seed) throws Exception {

    KeyGenerator keygen = KeyGenerator.getInstance("AES");
    SecureRandom secrand = SecureRandom.getInstance("SHA1PRNG");
    secrand.setSeed(seed.getBytes());
    keygen.init(128, secrand);

    SecretKey seckey = keygen.generateKey();
    byte[] rawKey = seckey.getEncoded();

    SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    return cipher.doFinal(data);
}

public static byte[] decrypt(byte[] data, String seed) throws Exception {

    KeyGenerator keygen = KeyGenerator.getInstance("AES");
    SecureRandom secrand = SecureRandom.getInstance("SHA1PRNG");
    secrand.setSeed(seed.getBytes());
    keygen.init(128, secrand);

    SecretKey seckey = keygen.generateKey();
    byte[] rawKey = seckey.getEncoded();

    SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    return cipher.doFinal(data);
}
