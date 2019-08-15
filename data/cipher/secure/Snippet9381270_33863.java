Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
AlgorithmParameters.getInstance("AES");
cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(iv));
byte[] decrypted = cipher.doFinal(data);
