public byte[] encrypt(String plainText) throws Exception {        
    //get salt
    salt = generateSalt();      
    byte[] saltBytes = salt.getBytes("UTF-8");

    // Derive the key
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec spec = new PBEKeySpec(
            password.toCharArray(), 
            saltBytes, 
            pswdIterations, 
            keySize
            );

    SecretKey secretKey = factory.generateSecret(spec);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

    //encrypt the message
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = cipher.getParameters();
    ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
    byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
    return Base64.encode(encryptedTextBytes);
}
