     PublicKey publicKey = x509cert.getPublicKey();
    //publinKey is key extracted from iOS device response stream

    Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    clearText = payloadContents.getBytes();
    //payloadContents are contents to be encrypyted
    cipherText = cipher.doFinal(clearText);
