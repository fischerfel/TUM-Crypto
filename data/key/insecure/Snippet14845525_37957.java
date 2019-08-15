byte[] key = "1234567890ABCDEF1234567890ABCDEF".getBytes("UTF-8");
byte[] iv  = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
SecretKeySpec newKey = new SecretKeySpec(key, "AES");
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
byte[] results = cipher.doFinal("This is a test".getBytes("UTF-8"));

return Base64.encodeToString(results,Base64.DEFAULT);
