byte[] encrypted = UniversalBase64Encoder.decode(input);
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.DECRYPT_MODE, generateAESKey128b(key));
byte[] originalBytes = cipher.doFinal(encrypted);
