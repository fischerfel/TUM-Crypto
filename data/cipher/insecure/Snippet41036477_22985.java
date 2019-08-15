Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.DECRYPT_MODE, publicKey);
inputStream = new CipherInputStream(new FileInputStream(encryptedFile), cipher);
outputStream = new FileOutputStream(decryptedFileName);
