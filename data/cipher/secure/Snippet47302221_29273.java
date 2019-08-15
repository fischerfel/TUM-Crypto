public  Cipher getCipher(int mode) throws Exception {

      Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding", new SunJCE());
      //a random Init. Vector. just for testing
      byte[] iv = listOfDecodedkeys.get(0).getBytes(AbstractSecurity.UTF_ENCODING);
      c.init(mode, generateKey(), new IvParameterSpec(iv));
      return c;
  }
