 private   Key generateKey() throws Exception {
      //PBKDF2WithHmacSHA256
      //SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
      char[] password = listOfDecodedkeys.get(1).toCharArray();
      byte[] salt = listOfDecodedkeys.get(2).getBytes(AbstractSecurity.UTF_ENCODING);
      KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
      SecretKey tmp = factory.generateSecret(spec);
      byte[] encoded = tmp.getEncoded();
      return new SecretKeySpec(encoded, "AES");

  }
