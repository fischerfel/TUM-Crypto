    public void decrypt(){
          int iterationCount = 1000;
          int keyLength = 256;
          int saltLength = keyLength / 8; 
          SecureRandom random = new SecureRandom();
          byte[] salt = new byte[saltLength];
          random.nextBytes(salt);
          KeySpec keySpec = new PBEKeySpec(str_key.toCharArray(), salt,  iterationCount, keyLength);
          SecretKeyFactory  keyFactory = null;
          try {
               keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
              } catch (NoSuchAlgorithmException e) {
                 e.printStackTrace();
              }
              byte[] keyBytes = new byte[0];
              try {
                keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
                } catch (InvalidKeySpecException e) {
                  e.printStackTrace();
                }
              SecretKey key = new SecretKeySpec(keyBytes, "Blowfish");



    Cipher cipher2 = null;
    try {
        cipher2 = Cipher.getInstance("Blowfish/CBC/PKCS5Padding"); 
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        e.printStackTrace();
    }
    iv = new byte[cipher2.getBlockSize()];
    random.nextBytes(iv);
    IvParameterSpec ivSpec = new IvParameterSpec(iv);
    try {
        cipher2.init(Cipher.DECRYPT_MODE, key, ivSpec );
    } catch (InvalidKeyException  | InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    }
    byte[] decryptedBytes = null;
    byte[] app= Base64.decode(str2,Base64.DEFAULT);

    try {
        decryptedBytes = cipher2.doFinal(app);
    } catch (IllegalBlockSizeException | BadPaddingException  e) {
        e.printStackTrace();
    }


    str3 = Base64.encodeToString(decryptedBytes,Base64.DEFAULT);
}
