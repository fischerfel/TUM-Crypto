public static String encrypt(String seed, String cleartext) throws Exception 
{
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    sr.setSeed(seed);
    kgen.init(128, sr); // 192 and 256 bits may not be available
    SecretKey skey = kgen.generateKey();

    byte[] rawKey = skey.getEncoded();
    SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(cleartext.getBytes());
    return toHex(encrypted);
}
