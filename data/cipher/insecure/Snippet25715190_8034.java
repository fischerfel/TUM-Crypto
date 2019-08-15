public static String[] encrypt(String str) throws NoSuchAlgorithmException,
        NoSuchPaddingException, InvalidKeyException,
        UnsupportedEncodingException, IllegalBlockSizeException,
        BadPaddingException {
    String Key, res;
    SecretKey key;
    String[] Return = new String[2];

    key = KeyGenerator.getInstance("DES").generateKey();
    ecipher = Cipher.getInstance("DES");
    ecipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] utf8 = str.getBytes("UTF8");
    byte[] enc = ecipher.doFinal(utf8);

    enc = BASE64EncoderStream.encode(enc);
    res = new String(enc);

    // Returning values 0 = Encrypted String 1 = Key For Storage in XML
    Return[0] = res;
    byte[] keyBytes = key.getEncoded();
    Key = new String(BASE64EncoderStream.encode(keyBytes), "UTF8");
    Return[1] = Key;

    return Return;
}

public static String decrypt(String str, String Key)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException,
        UnsupportedEncodingException, BadPaddingException {
    SecretKey key = new SecretKeySpec(BASE64DecoderStream.decode(Key.getBytes("UTF8")), "DES");
    dcipher = Cipher.getInstance("DES");
    dcipher.init(Cipher.DECRYPT_MODE, key);
    byte[] dec = BASE64DecoderStream.decode(str.getBytes());
    byte[] utf8 = dcipher.doFinal(dec);
    return new String(utf8, "UTF8");
}
