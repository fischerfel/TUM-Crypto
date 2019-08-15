  private static byte[] decryptMessage(byte[] file, byte[] iVector, byte[] aesKey) throws Exception {
    IvParameterSpec spec = new IvParameterSpec(Arrays.copyOfRange(iVector, 0, 16));
    SecretKeySpec key = new SecretKeySpec(Arrays.copyOfRange(aesKey, 0, 16), "AES");
    Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, key, spec);
    return cipher.doFinal(file);
  }
