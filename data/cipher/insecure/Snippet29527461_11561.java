data = EFjTatx2VkAZR3ScS0UadQr8M6zEkIz/kAX0Cl+XH2FNNHVbeJsEd2b+zWlEkvR6;//( it is base64 encoded )
Cipher aescipher = Cipher.getInstance("AES/ECB/NoPadding", "SunJCE");
Key keySpec = new SecretKeySpec(clefAES, "AES");                   
byte[] encryptedTextByte = decoder.decodeBuffer(data);
aescipher.init(Cipher.DECRYPT_MODE, keySpec);
byte[] decryptedByte = aescipher.doFinal(encryptedTextByte);
String decryptedText = new String(decryptedByte);
