String text = "hello";
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();
        Key publicKey = kp.getPublic();
        Key privateKey = kp.getPrivate();

        Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherData = cipher.doFinal(text.getBytes());

        cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] textData = cipher.doFinal(text.getBytes());

        String decrypted = new String(textData);
        System.out.println(decrypted);
