public Cipher getCipher(byte[] password) {

    MessageDigest md = MessageDigest.getInstance("SHA-1");

    byte keyData[] = createKey(byte[] password, md);

     SecretKey secretKey = 
        SecretKeyFactory.getInstance("DESede").
        generateSecret(new DESedeKeySpec(keyData[]));

    IVSpec ivspec = createIV(secretKey.getEncoded(), md);

    Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    cipher.init(1, secretKey, ivSpec, md);

    return cipher;
}
