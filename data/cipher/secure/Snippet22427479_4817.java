private static byte[] password = null; //  this.password = editText.getBytes();
static final byte[] ivBytes = {'6','g','6','o','d','a','0','u','4','n','w','i','6','9','i','j'};

public static byte[] encrypt(String text) throws Exception {
    byte[] clear = text.getBytes("UTF-8");
    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    sr.setSeed(password);
    kgen.init(256, sr); // 192 and 256 bits may not be available
    SecretKey skey = kgen.generateKey();
    byte[] key = skey.getEncoded();

    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
    byte[] encrypted = cipher.doFinal(clear);
    return encrypted;
}

public static String decrypt(byte[] encrypted) throws Exception {
    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    sr.setSeed(password);
    kgen.init(256, sr); // 192 and 256 bits may not be available
    SecretKey skey = kgen.generateKey();
    byte[] key = skey.getEncoded();

    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
    String decrypted = new String(cipher.doFinal(encrypted));
    return decrypted;
}
