 Cipher cipher = Cipher.getInstance("RSA");
 cipher.init(Cipher.DECRYPT_MODE, key);
 Path path = Paths.get("fileToDecrypt.p7m");
 byte[] data = Files.readAllBytes(path);
 byte[] decryptedData = cipher.doFinal(data);
