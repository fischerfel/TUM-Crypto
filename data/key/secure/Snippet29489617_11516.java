// zero filled input vector
byte[] ivData = new byte[32];
IvParameterSpec iv = new IvParameterSpec(ivData);
SecretKeySpec keySpec = new SecretKeySpec(keyData, "AES");
Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
aes.init(Cipher.ENCRYPT_MODE, keySpec, iv);
byte[] cipherText = aes.doFinal(data);
