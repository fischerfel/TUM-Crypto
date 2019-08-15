byte[] encrypt(byte[] clearData) {
   byte[] passwordKey = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E,0x0f};
   byte[] rawSecretKey = new byte[]{0x34, (byte) 0xA4, 0x16, 0x09, 0x77, (byte) 0x85, (byte) 0xB4, 0x31,
                                                0x75, 0x12, (byte) 0x92, (byte) 0xDD, (byte) 0xCA, 0x15, (byte) 0xAB, (byte) 0xBA};
   secretKey = new SecretKeySpec(passwordKey, "AES");
   ivParameterSpec = new IvParameterSpec(rawSecretKey);
   Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
   aesCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

   byte[] encryptedData;
   encryptedData = aesCipher.doFinal(clearData);

   return encryptedData;
}
