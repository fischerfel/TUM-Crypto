SecretKey key = getEncryptionKey(); 
byte[] iv = new byte[] { 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, (byte)144, (byte)233, 122, 100 };
byte[] cipherBytes = readEncryptedFile();
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
IvParameterSpec ivParams = new IvParameterSpec(iv);
cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
byte[] plaintext = cipher.doFinal(cipherBytes);
