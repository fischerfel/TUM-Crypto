private static Cipher getCipher(String key, int mode) throws Exception{
     byte[] rawKey = getRawKey(key.getBytes("UTF-8"));
     SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
     Key key2 = skeySpec;
     Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
     cipher.init(mode, key2);
     return cipher;
}

private static byte[] getRawKey(byte[] seed) throws Exception {
/*  BEFORE:
     KeyGenerator kgen = KeyGenerator.getInstance("AES");
     SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
     sr.setSeed(seed);
     kgen.init(128, sr); // 192 and 256 bits may not be available
     SecretKey skey = kgen.generateKey();
     byte[] raw = skey.getEncoded();
*/
     byte[] raw = MD5Util.getMD5HashRaw(seed);
     return raw;
}
