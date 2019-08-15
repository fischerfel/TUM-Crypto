Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
SecretKeySpec keySpec = new SecretKeySpec("0123456789012345".getBytes(), "AES");
IvParameterSpec ivSpec = new IvParameterSpec("0123459876543210".getBytes());
cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

outputStream = new CipherOutputStream(output_stream, cipher);
