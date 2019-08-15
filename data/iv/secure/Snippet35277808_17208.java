  static RSAPrivateKey decrypt(String keyDataStr, String ivHex, String password)
    throws GeneralSecurityException
  {
    byte[] pw = password.getBytes(StandardCharsets.UTF_8);
    byte[] iv = h2b(ivHex);
    SecretKey secret = opensslKDF(pw, iv);
    Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
    byte[] pkcs1 = cipher.doFinal(Base64.getMimeDecoder().decode(keyDataStr));
    /* See note for definition of "decodeRSAPrivatePKCS1" */
    RSAPrivateCrtKeySpec spec = decodeRSAPrivatePKCS1(pkcs1);
    KeyFactory rsa = KeyFactory.getInstance("RSA");
    return (RSAPrivateKey) rsa.generatePrivate(spec);
  }

  private static SecretKey opensslKDF(byte[] pw, byte[] iv)
    throws NoSuchAlgorithmException
  {
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    md5.update(pw);
    md5.update(iv);
    byte[] d0 = md5.digest();
    md5.update(d0);
    md5.update(pw);
    md5.update(iv);
    byte[] d1 = md5.digest();
    byte[] key = new byte[24];
    System.arraycopy(d0, 0, key, 0, 16);
    System.arraycopy(d1, 0, key, 16, 8);
    return new SecretKeySpec(key, "DESede");
  }

  private static byte[] h2b(CharSequence s)
  {
    int len = s.length();
    byte[] b = new byte[len / 2];
    for (int src = 0, dst = 0; src < len; ++dst) {
      int hi = Character.digit(s.charAt(src++), 16);
      int lo = Character.digit(s.charAt(src++), 16);
      b[dst] = (byte) (hi << 4 | lo);
    }
    return b;
  }
