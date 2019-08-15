public static String encrypt(String seed, String cleartext) throws Exception {
    byte[] rawKey = getRawKey(seed.getBytes(), seed);
    byte[] result = encrypt(rawKey, cleartext.getBytes());
    return toHex(result); // "unlock code" which must always be the same for the same seed and clearText accross android versions
}

private static byte[] getRawKey(byte[] seed, String seedStr) throws Exception {
    SecureRandom sr;
    sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");  // what used to work
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    sr.setSeed(seed);
    kgen.init(128, sr); 
    SecretKey skey = kgen.generateKey();
    byte[] raw = skey.getEncoded();
    return raw;
}

private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(clear);
    return encrypted;
}

public static String toHex(byte[] buf) {
    if (buf == null)
        return "";
    StringBuffer result = new StringBuffer(2 * buf.length);
    for (int i = 0; i < buf.length; i++) {
        appendHex(result, buf[i]);
    }
    return result.toString();
}
