    public SecretKey getAESkey() throws Exception, NoSuchAlgorithmException{        
      KeyGenerator generator = KeyGenerator.getInstance("AES");
      generator.init(128);
      SecretKey sKey = generator.generateKey();
      return sKey;  // will be passed to encryptSecretKey method
   }

    public byte[] encryptSecretKey (SecretKey sKey)
    {
      Cipher cipher = null;
      byte[] key = null;

      try
      {
        // initialize the cipher with the user's public key
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyHolder.keyPair.getPublic() );
        key = cipher.doFinal(sKey.getEncoded());
      }
      catch(Exception e )
      {
         e.printStackTrace();
      }
      return key;
  }
