    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    IvParameterSpec ivparameters = new IvParameterSpec(IV);
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(cipherKey, "AES"), ivparameters);

              //and here I have to read a string sent from client

    String textToRead = (String) ois.readObject();
    String decryptedText = new String(cipher.doFinal(textToRead.getBytes()));
