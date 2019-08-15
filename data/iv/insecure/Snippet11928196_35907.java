public byte[] encrypt(byte[] key, byte[] message) throws Exception {

       byte [] plainTextBytes = message;
       byte[] encryptKey = key;

       SecretKey theKey = new SecretKeySpec(encryptKey, "DESede");
       Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
       IvParameterSpec IvParameters = new IvParameterSpec(new byte[] {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00});
       cipher.init(Cipher.ENCRYPT_MODE, theKey, IvParameters);
       byte[] encrypted = cipher.doFinal(plainTextBytes);
       return encrypted;
  }
