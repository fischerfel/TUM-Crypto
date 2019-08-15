    PublicKey pubKey = readPublicKeyFromFile(mod, ex);
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
    byte[] cipherData = cipher.doFinal(data);
    return cipherData;
