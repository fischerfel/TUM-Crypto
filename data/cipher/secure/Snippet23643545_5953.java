public byte[] rsaDecrypt(byte[] data) throws Exception, BadPaddingException {

      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.DECRYPT_MODE, privKey);
      byte[] clearData = cipher.doFinal(data);
      return clearData;
    }
