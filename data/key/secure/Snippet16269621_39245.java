byte bufferBytes[] = Base64.decode(password);
SecretKey sk = new SecretKeySpec(bufferBytes, 0, bufferBytes.length, "AES");
