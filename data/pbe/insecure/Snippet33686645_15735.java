      private static Cipher getCipher(int mode) throws Exception {
      Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");

      //a random Init. Vector. just for testing
      byte[] iv = "e675f725e675f725".getBytes("UTF-8");

      c.init(mode, generateKey(), new IvParameterSpec(iv));
      return c;
  }


  private static String Decrypt(String encrypted) throws Exception {

      byte[] decodedValue = new Base64().decode(encrypted.getBytes("UTF-8")); // new BASE64Decoder().decodeBuffer(encrypted);

      Cipher c = getCipher(Cipher.DECRYPT_MODE);
      byte[] decValue = c.doFinal(decodedValue);

      return new String(decValue);
  }

  private static Key generateKey() throws Exception {
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      char[] password = "3x5FBNs!".toCharArray();
      byte[] salt = "S@1tS@1t".getBytes("UTF-8");

      KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
      SecretKey tmp = factory.generateSecret(spec);
      byte[] encoded = tmp.getEncoded();
      return new SecretKeySpec(encoded, "AES");
  }
