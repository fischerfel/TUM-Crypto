public void enc(){
    byte[] rawKey = getRawKey("my_key".getBytes());
    SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal("my_message".getBytes());
    String result=Base64.encodeToString(encrypted, Base64.DEFAULT);
}

private static byte[] getRawKey(byte[] seed) throws Exception {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    sr.setSeed(seed);
    kgen.init(256, sr); 
    SecretKey skey = kgen.generateKey();
    byte[] raw = skey.getEncoded();
    return raw;
}
