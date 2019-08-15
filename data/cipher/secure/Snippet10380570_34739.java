    Cipher cipher = Cipher.getInstance("RSA");
    PrivateKey privateKey = keyPair.getPrivate();
    // decrypt the ciphertext using the private key 
    cipher.init(Cipher.DECRYPT_MODE, privateKey); 
    byte[] decryptedText = cipher.doFinal(theBytes); 
