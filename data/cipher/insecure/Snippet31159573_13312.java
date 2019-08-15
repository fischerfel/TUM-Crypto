byte[] dataBytes = new byte []{(byte)0xf3,(byte)0x8b,(byte)0x0c,(byte)0xb3,(byte)0xa3,(byte)0x26,(byte)0x12,(byte)0x23,(byte)0xe0,(byte)0xe0,(byte)0x9f,(byte)0x1f,(byte)0x28,(byte)0x01,(byte)0x28,(byte)0x35};
SecretKeySpec secretKeySpec = new SecretKeySpec("keykeykeykeykey1".getBytes("UTF-8"), "AES");
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
byte[] decryptedData = cipher.doFinal(dataBytes); //this line throws exception
