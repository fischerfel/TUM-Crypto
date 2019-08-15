        Cipher cipher = Cipher.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pKey = keyFactory.generatePublic(keySpec);
        cipher.init(Cipher.ENCRYPT_MODE, pKey);
        encoded = cipher.doFinal(clearText.getBytes());
