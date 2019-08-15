public static String encrypt(String value, String key) {
    SecretKey secretKey;
    String ex;
    try {
        byte[] encodedKey = Base64.decode(key, Base64.DEFAULT);

        secretKey = new SecretKeySpec(encodedKey,
                "AES");
        Cipher encrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
        encrypt.init(Cipher.ENCRYPT_MODE, secretKey,ivSpec);
        byte[] valueDecode = value.getBytes("UTF-16");
        byte[] valueInput = Base64.decode(value, Base64.DEFAULT);
        byte[] valueOutput = encrypt.doFinal(valueInput);


        return 
                 Base64.encodeToString( valueOutput, Base64.DEFAULT );
    } catch (Exception e) {
        e.printStackTrace();
        ex = e.toString();
    }
//  Log.d(TAG, ex);
    return null;
}

private static String decrypt(String value, String key) {
    SecretKey secretKey;
    String ex;

    byte[] encodedKey;
    try {
        encodedKey = Base64.decode(key, Base64.DEFAULT);

        secretKey = new SecretKeySpec(encodedKey, 
                "AES");
        Cipher decrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decrypt.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] valueInput = Base64.decode(value,Base64.DEFAULT);
        byte[] valueOutput = decrypt.doFinal(valueInput);

        return Base64.encodeToString(valueOutput, Base64.DEFAULT);
    } catch ( Exception e) {
        e.printStackTrace();
    }
    return null;
}
