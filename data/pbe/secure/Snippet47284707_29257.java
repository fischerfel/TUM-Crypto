     private  Key generateKey() throws Exception {

     SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

      char[] password = listOfDecodedkeys.get(1).toCharArray();
      byte[] salt = listOfDecodedkeys.get(2).getBytes(AbstractSecurity.UTF_ENCODING);
      KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
      SecretKey tmp = factory.generateSecret(spec);
      byte[] encoded = tmp.getEncoded();
      return new SecretKeySpec(encoded, "AES");

  }
