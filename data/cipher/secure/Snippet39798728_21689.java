public static String encrypt(String toEncrypt) throws Exception 
  {

        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");

        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding"); 

        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));

        byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());
        byte[] encryptedValue = Base64.encodeBase64(encrypted);

        return new String(encryptedValue);
  }
