public static String encrypt(String data, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

    // Create AES secret key
    Cipher aes = Cipher.getInstance("AES");
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(256);
    SecretKey key = kgen.generateKey();
    SecretKeySpec aeskeySpec = new SecretKeySpec(key.getEncoded(), "AES");

    // Encrypt data with AES Secret key
    aes.init(Cipher.ENCRYPT_MODE, aeskeySpec);
    byte[] dataEncoded = aes.doFinal(data.getBytes());

    // Encrypt the secret AES key with the public key
    Cipher rsa = Cipher.getInstance("RSA");
    rsa.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] aesKeyEncoded = rsa.doFinal(key.getEncoded());

    // Output both secret AES key and data
    return
        Base64.getEncoder().encodeToString(aesKeyEncoded) + "~" +
        Base64.getEncoder().encodeToString(dataEncoded);

}
