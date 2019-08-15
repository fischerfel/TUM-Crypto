 Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[cipher.getBlockSize()]));
    byte[] encryptedBytes_updates = cipher.update(encryptedBytes);
    String decryptedText = new String(cipher.doFinal(encryptedBytes_updates));
