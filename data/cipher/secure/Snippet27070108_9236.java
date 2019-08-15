    byte[] decryptedData = null;
    System.out.println("Before Decryption:");
    System.out.println(tmp);

    byte [] byteArray = Base64.decode(tmp);

    try {
        privKey = pair.getPrivate();
        //  System.out.println("PRIVATE KEY IN FUNCTION"+privKey);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        // System.out.println(new String(byteArray, StandardCharsets.UTF_8));

        decryptedData = cipher.doFinal(byteArray);
        System.out.println("Decrypted Data: ");
        sentToDB = new String(decryptedData, StandardCharsets.UTF_8);
    }
