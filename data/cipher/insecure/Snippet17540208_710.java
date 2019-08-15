private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] decrypted = cipher.doFinal(encrypted);
    return decrypted;
}  

private static byte[] getRawKey(byte[] seed) throws Exception {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    sr.setSeed(seed);
    kgen.init(KEY_SIZE, sr);
    SecretKey skey = kgen.generateKey();
    byte[] raw = skey.getEncoded();
    return raw;
}  

public static byte[] toByte(String hexString) {
    int len = hexString.length() / 2;
    byte[] result = new byte[len];

    for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), END_POSITION).byteValue();
    }

    return result;
}

public static void generateKey() {
    try {
        byte[] seed = SEED_STRING.getBytes("UTF-8");
        byte[] rawKey = getRawKey(seed);           
        byte[] toDecrypt = toByte(Constants.ENCRYPTED);

        mKey = new String(decrypt(rawKey, toDecrypt), "UTF-8");      

    } catch (Exception e) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "encryption: ", e);
        }
    }   
}
