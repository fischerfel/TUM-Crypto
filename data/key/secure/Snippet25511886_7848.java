    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    IvParameterSpec ivparameters = new IvParameterSpec(IV);
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(cipherKey, "AES"), ivparameters);

             //and here I have to sent a string(the string is the SHA512 of a password

    byte[] encryptedText = cipher.doFinal(passwordHash.getBytes());
    oos.writeObject(new String(encryptedText));
    oos.flush();
