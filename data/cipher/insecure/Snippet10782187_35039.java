public static String encrypt(String seed, String cleartext) throws Exception  
{
    byte[] rawKey = getRawKey(seed.getBytes());
    byte[] result = encrypt(rawKey, cleartext.getBytes()); 
    return toHex(result);
}

private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception 
{
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(clear);
    return encrypted;
}
