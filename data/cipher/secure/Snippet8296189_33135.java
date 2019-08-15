Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
SecretKeySpec keySpec = new SecretKeySpec("01234567890abcde".getBytes(), "AES");
IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210".getBytes());
cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
ByteArrayInputStream input = new ByteArrayInputStream(mediaCollBuffer); 
CipherInputStream cis = new CipherInputStream(input, cipher);
