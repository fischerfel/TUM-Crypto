public class VincentFileCrypto {

public  byte[] encrypt(String seed, byte[] cleartext) throws Exception {
    byte[] rawKey = getRawKey(seed.getBytes());
    byte[] result = encrypt(rawKey, cleartext);
    //  return toHex(result);
    return result;
}

public  byte[] decrypt(String seed, byte[] encrypted) throws Exception {
    byte[] rawKey = getRawKey(seed.getBytes());
    byte[] enc = encrypted;
    byte[] result = decrypt(rawKey, enc);
    return result;
}

//done
private  byte[] getRawKey(byte[] seed) throws Exception {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    sr.setSeed(seed);
    kgen.init(128, sr); // 192 and 256 bits may not be available
    SecretKey skey = kgen.generateKey();
    byte[] raw = skey.getEncoded();
    return raw;
}

private  byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(clear);
    return encrypted;
}

private  byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] decrypted = cipher.doFinal(encrypted);
    return decrypted;
}
