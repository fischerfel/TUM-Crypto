byte[] digest = MessageDigest.getInstance("SHA-1").digest(password);
SecretKey secretKey = new SecretKeySpec(Arrays.copyOf(digest, 16), "AES");

Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, secretKey);

return cipher.doFinal(serializeLicense(data));
