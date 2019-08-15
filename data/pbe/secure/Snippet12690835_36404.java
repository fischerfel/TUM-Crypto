public String decrypt(String ciphertext) throws Exception {
    byte[] crypt = Base64.decodeBase64(ciphertext);

    // parse the encrypted data and get salt and IV
    byte[] salt = Arrays.copyOfRange(crypt, 0, saltLength);
    byte[] iv = Arrays.copyOfRange(crypt, saltLength, saltLength + ivLength);
    byte[] encryptedData = Arrays.copyOfRange(crypt, saltLength + ivLength, crypt.length);

    // generate key from salt and password  
    SecretKeyFactory f = SecretKeyFactory.getInstance(secretKeyName);
    KeySpec ks = new PBEKeySpec(password.toCharArray(), salt, pbkdfNumIters, keyLength);
    SecretKey s = f.generateSecret(ks);
    Key keySpec = new SecretKeySpec(s.getEncoded(),"AES");

    // initialize the cipher object with the key and IV
    Cipher cipher = Cipher.getInstance(cipherAlgo);
    IvParameterSpec ivSpec = new IvParameterSpec(iv);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

    // decrypt
    byte[] decBytes = cipher.doFinal(encryptedData);

    return new String(decBytes);
}
