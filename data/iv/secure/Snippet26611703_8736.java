SecretKey keySpec = new SecretKeySpec(keyBytes, "AES");
AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
Cipher aesCbc = Cipher.getInstance("AES/CBC/NoPadding");
aesCbc.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
byte[] encryptedBytes = aesCbc.doFinal(plainTextBytes);
return Base64.encodeBase64String(encryptedBytes); 
