SecretKeySpec keySpec = new SecretKeySpec(AesKey, "AES");
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, keySpec);
return cipher.doFinal(input);
