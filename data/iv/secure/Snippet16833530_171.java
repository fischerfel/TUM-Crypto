  public static String encrypt(String plaintext)
    throws Exception
  {
    Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sharedkey, "DESede"), new IvParameterSpec(sharedvector));
    byte[] encrypted = c.doFinal(plaintext.getBytes("UTF-8"));
    return Base64.encode(encrypted);
  }
