Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
aes.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
String cleartext = new String(aes.doFinal(ciphertext));
