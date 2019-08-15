    public String aesEncrypt(String key, String data) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
          SecretKey secKey = new SecretKeySpec(key.getBytes(), "AES");
          KeyGenerator KeyGen = KeyGenerator.getInstance("AES");
          KeyGen.init(256);
          Cipher AesCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
          AesCipher.init(Cipher.ENCRYPT_MODE, secKey, new IvParameterSpec(IV.getBytes("UTF-8")));
          byte[] byteCipherText = AesCipher.doFinal(data.getBytes());
          return Base64.encodeToString(byteCipherText, 0).trim();
    }
