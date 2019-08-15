    KeyGenerator keyGenS = KeyGenerator.getInstance("AES");
    keyGenS.init(128);
    SecretKey sKey1 = keyGenS.generateKey();

    Cipher aesCipher = Cipher.getInstance("AES");
    aesCipher.init(Cipher.ENCRYPT_MODE,sKey1);

    byte[] byteText = "Insert here whatever you want to crypt".getBytes();

    byte[] byteCipherText = aesCipher.doFinal(byteText);
