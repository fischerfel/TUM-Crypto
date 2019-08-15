Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");

// setup an IV (initialization vector) that should be
// randomly generated for each input that's encrypted
byte[] iv = new byte[cipher.getBlockSize()];
new SecureRandom().nextBytes(iv);
IvParameterSpec ivSpec = new IvParameterSpec(iv);

// decrypt
SecretKey secretKey = new SecretKeySpec(Base64.decodeBase64(keyString), "Blowfish");
cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
byte[] decrypted = cipher.doFinal(Base64.decodeBase64(input));
return Hex.encodeHexString(decrypted);
