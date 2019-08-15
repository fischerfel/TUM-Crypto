    byte[] dataToEncrypt = temp.getBytes(StandardCharsets.UTF_8); //temp is a String
    byte[] encryptedData = null;

    Cipher cipher = Cipher.getInstance("RSA");
    //cipher.init(Cipher.ENCRYPT_MODE, serverPubKey);
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
    encryptedData = cipher.doFinal(dataToEncrypt);
    String cipherStr = Base64.encodeToString(encryptedData,Base64.DEFAULT);
