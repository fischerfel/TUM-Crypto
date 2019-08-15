    PrivateKey pubKey = readPrivateKeyFromFile(mod, ex);
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, pubKey);
    byte[] cipherData = cipher.doFinal(data);
    return cipherData;
