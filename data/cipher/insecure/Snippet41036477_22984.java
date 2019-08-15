    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    outputStream = new CipherOutputStream(new FileOutputStream(encryptedFile), cipher);
