        String strDataToEncrypt = new String();
        String strCipherText = new String();
        String strDecryptedText = new String();

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();

        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE,secretKey);

        strDataToEncrypt = "any text input";
        byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
        byte[] byteCipherText = aesCipher.doFinal(byteDataToEncrypt); 
        strCipherText = new BASE64Encoder().encode(byteCipherText);
        System.out.println("cipher text: " +strCipherText);
        aesCipher.init(Cipher.DECRYPT_MODE,secretKey,aesCipher.getParameters());
        byte[] byteDecryptedText = aesCipher.doFinal(new BASE64Decoder().decodeBuffer(strCipherText));
        strDecryptedText = new String(byteDecryptedText);
        System.out.println("plain text again: " +strDecryptedText);
