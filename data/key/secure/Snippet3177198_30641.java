   Cipher aesCipher = Cipher.getInstance(AES_ALGORITHM_MODE_PADDING, PROVIDER);
   SecretKeySpec aeskeySpec = new SecretKeySpec(rawAesKey, AES);
   aesCipher.init(Cipher.ENCRYPT_MODE, aeskeySpec);
   byte[] encryptedData = aesCipher.doFinal(data);
   this.iv = Base64.encodeBase64(aesCipher.getIV()); //get hold of the random IV

   return encryptedData;
