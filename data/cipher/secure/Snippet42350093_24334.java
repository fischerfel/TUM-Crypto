public static String AESEncrypt(String text, String key, String iv) throws Exception {
     Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
     byte[] keyBytes = new byte[16];
     byte[] b = key.getBytes("UTF-8");
     int len = b.length;
     if (len > keyBytes.length)
         len = keyBytes.length;
     System.arraycopy(b, 0, keyBytes, 0, len);

     byte[] keyBytesiv = new byte[16];
     byte[] biv = iv.getBytes("UTF-8");
     int leniv = biv.length;
     if (leniv > keyBytesiv.length)
         leniv = keyBytesiv.length;
     System.arraycopy(biv, 0, keyBytes, 0, len);
     SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
     IvParameterSpec ivSpec = new IvParameterSpec(keyBytesiv);

     cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

     byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
     BASE64Encoder encoder = new BASE64Encoder();
     return encoder.encode(results);
 }
