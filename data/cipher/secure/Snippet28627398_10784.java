public String encrypt(String stringToEncrypt, String userKey)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    // User gives string key which is formatted to 16 byte and to a secret
    // key
    byte[] key = userKey.getBytes();
    MessageDigest sha = MessageDigest.getInstance("SHA-1");
    key = sha.digest(key);
    key = Arrays.copyOf(key, 16);
    SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

    // Cipher initialization
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    // Encryption and encoding
    String encryptedData = new BASE64Encoder().encode(cipher
            .doFinal(stringToEncrypt.getBytes()));

    // IV is printed to user
    System.out.println("\nENCRYPTION IV: \n"
            + new BASE64Encoder().encode(cipher.getIV()) + "\n");

    // Function returns encrypted string which can be writed to text file
    return encryptedData;

}

public String decrypt(String stringToDecrypt, String userKey, String userIv)
        throws NoSuchAlgorithmException, IOException,
        NoSuchPaddingException, InvalidKeyException,
        InvalidAlgorithmParameterException, IllegalBlockSizeException,
        BadPaddingException {

    // User gives the same string key which was used for encryption
    byte[] key = userKey.getBytes();
    MessageDigest sha = MessageDigest.getInstance("SHA-1");
    key = sha.digest(key);
    key = Arrays.copyOf(key, 16);
    SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

    // Decode string iv to byte
    byte[] iv = new BASE64Decoder().decodeBuffer(userIv);

    // Cipher initialization
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

    // Decryption and decoding
    String decryptedData = new String(cipher.doFinal(new BASE64Decoder()
            .decodeBuffer(stringToDecrypt)));

    // Function returns decrypted string which can be writed to text file
    return decryptedData;

}
