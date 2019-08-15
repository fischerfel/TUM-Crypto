 private  byte[] getRawKey(byte[] seed) throws Exception {

    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG","Crypto");
    sr.setSeed(seed);
    kgen.init(128, sr); // 192 and 256 bits may not be available
    SecretKey skey = kgen.generateKey();
    byte[] raw = skey.getEncoded();
    return raw;
}
  private  byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {

    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
  // Cipher cipher = Cipher.getInstance("AES");
    IvParameterSpec ivSpec = new IvParameterSpec(raw);

    Cipher cipher = Cipher.getInstance("CBC");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec,ivSpec);
    byte[] decrypted = cipher.doFinal(encrypted);
    return decrypted;
}
