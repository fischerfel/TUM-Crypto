    Cipher aesCipher = Cipher.getInstance("AES/GCM/NoPadding");
    aesCipher.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(128, iv));

    byte[] block = new byte[1073741824];
    int i;
    try (FileOutputStream fos = new FileOutputStream(decryptedFile)) {
        while ((i = is.read(block)) != -1) {
            byte[] decryptedBlock = aesCipher.update(block, 0, i);
            if (decryptedBlock != null) {
                fos.write(decryptedBlock);
            }
        }
        byte[] decryptedFinal = aesCipher.doFinal();
        if (decryptedFinal != null) {
            fos.write(decryptedFinal);
        }
    }
