Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
SecretKeySpec keySpec = new SecretKeySpec(CHUNK_ENCRYPTION_KEY.getBytes(), 0, 32, "AES");
IvParameterSpec initVector = new IvParameterSpec(AES_INITIALIZATION_VECTOR.getBytes(), 0 , 16);
cipher.init(Cipher.ENCRYPT_MODE, keySpec, initVector);
