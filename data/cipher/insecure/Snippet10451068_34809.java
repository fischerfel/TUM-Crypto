// Java - Encrypt
private String EncryptAES(String text,String key) throws Exception
    {
      SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");

      // Instantiate the cipher
      Cipher cipher = Cipher.getInstance("AES");

      cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
      byte[] encrypted = cipher.doFinal(text.getBytes());

      String encrypttext = new BASE64Encoder().encode(encrypted);

      return encrypttext;
    }

RESULT: TeUZAFxoFoQy/roPm5tXyPzJP/TLAwR1aIGn2xHbZpsbY1qrKwXfO+F/DAqmeTwB0b8e6dsSM+Yy0zrQt22E2Q== 
