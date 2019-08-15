    ObjectInputStream inside = new ObjectInputStream(s.getInputStream());
    byte[] bytesToDecrypt = (byte[])inside.readObject();

    cd.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] decryptedBytes = cd.doFinal(bytesToDecrypt);
    SecretKey decrypted = new SecretKeySpec(decryptedBytes, 0, decryptedBytes.length, "AES");
