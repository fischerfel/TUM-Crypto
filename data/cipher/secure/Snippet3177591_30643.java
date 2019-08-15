Cipher cipherAlg = Cipher.getInstance("AES/CTR/NoPadding", PROVIDER);
byte[] ivBytes = new byte[cipherAlg.getBlockSize()];
(new SecureRandom()).nextBytes(ivBytes);
cipherAlg.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(ivBytes));
byte[] cipher = cipherAlg.doFinal(plainText);
byte[] cipherText = new byte[ivBytes.length + cipher.length];
System.arraycopy(ivBytes, 0, cipherText, 0, ivBytes.length);
System.arraycopy(cipher, 0, cipherText, ivBytes.length, cipher.length);
return cipherText;
