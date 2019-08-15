        createPrivateKey();
        createPublicKey();

        String data = "12";

        Cipher cipher1 = Cipher.getInstance("RSA/ECB/NoPadding");
        cipher1.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedData = cipher1.doFinal(data.getBytes());

        Cipher cipher2 = Cipher.getInstance("RSA/ECB/NoPadding");
        cipher2.init(Cipher.DECRYPT_MODE,privateKey);
        byte[] decryptedData = cipher2.doFinal(encryptedData);
        System.out.println(new String(decryptedData));
