public static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {

    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(clear);
    return encrypted;
}

public static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {

    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] decrypted = cipher.doFinal(encrypted);
    return decrypted;
}

public static byte[] getRaw(String password_) throws Exception {

    byte[] keyStart = password_.getBytes();
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
    sr.setSeed(keyStart);
    kgen.init(128, sr); 
    SecretKey skey = kgen.generateKey();
    byte[] key = skey.getEncoded();  

    return key;
}
