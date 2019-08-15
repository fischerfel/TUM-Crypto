public static byte[] encryptAES(String seed, String cleartext)
        throws Exception {
    byte[] rawKey = getRawKey(seed.getBytes("ASCII"));
    SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    return cipher.doFinal(cleartext.getBytes("ASCII"));
}

private static byte[] getRawKey(byte[] seed) throws Exception {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    sr.setSeed(seed);
    kgen.init(BLOCKS, sr); // 192 and 256 bits may not be available
    SecretKey skey = kgen.generateKey();
    byte[] raw = skey.getEncoded();
    return raw;
}

public static void main(String [] args)
{
    String str = "312432432";
    String key = "4AFJ3243J";
    String result = new String(encryptAES(key,str), "ASCII");
}
