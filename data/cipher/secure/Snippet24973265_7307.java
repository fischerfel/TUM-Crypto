public String encrypt(String plainText) throws Exception {   
    //get salt    
    byte[] saltBytes = generateSalt(saltLength);

    // Derive the key
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, pswdIterations, keySize);

    SecretKey secretKey = factory.generateSecret(spec);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

    //encrypt the message
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = cipher.getParameters();
    byte[] ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
    byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));

    //prepend the salt and IV
    byte[] buffer = new byte[saltBytes.length + ivBytes.length + encryptedTextBytes.length];
    System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);
    System.arraycopy(ivBytes, 0, buffer, saltBytes.length, ivBytes.length);
    System.arraycopy(encryptedTextBytes, 0, buffer, saltBytes.length + ivBytes.length, encryptedTextBytes.length);
    return new Base64().encodeToString(buffer);
}

public String decrypt(String encryptedText) throws Exception {

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    //strip off the salt and IV
    ByteBuffer buffer = ByteBuffer.wrap(new Base64().decode(encryptedText));
    byte[] saltBytes = new byte[saltLength];
    buffer.get(saltBytes, 0, saltBytes.length);
    byte[] ivBytes = new byte[cipher.getBlockSize()];
    buffer.get(ivBytes, 0, ivBytes.length);
    byte[] encryptedTextBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes.length];
    buffer.get(encryptedTextBytes);

    // Derive the key
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, pswdIterations, keySize);

    SecretKey secretKey = factory.generateSecret(spec);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));

    byte[] decryptedTextBytes = null;
    try {
        decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    }

    return new String(decryptedTextBytes);
}
