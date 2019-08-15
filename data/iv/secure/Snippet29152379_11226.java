Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"),
        new IvParameterSpec(iv));
byte[] recoveredPlaintextBytes = cipher.doFinal(ciphertextBytes);
String recoveredPlaintext = new String(recoveredPlaintextBytes);
