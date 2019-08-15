public static String enc(String message) {

    byte[] keyInBytes = KEY.getBytes();
    byte[] encryptedData = null;
    KeyGenerator kgen;
    try {
        kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(keyInBytes);

        kgen.init(256, secureRandom);

        SecretKey skey = kgen.generateKey();

        key = skey.getEncoded();

        try {
            encryptedData = encrypt(key, message.getBytes());
            Log.w("ENC ", new String(encryptedData));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    } catch (NoSuchAlgorithmException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }

    return Base64.encodeToString(encryptedData, Base64.DEFAULT);

    // return message;
}

private static byte[] encrypt(byte[] raw, byte[] testMessage)
        throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, /* "PBEWithSHA1And128BitAES-CBC-BC" */
    "AES");
    final byte[] iv = KEY.getBytes();

    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
    byte[] encrypted = cipher.doFinal(testMessage);
    try {
        decrypt(raw, encrypted);
    } catch (Exception e) {
        e.getMessage();
        // TODO: handle exception
    }
    return encrypted;
}
