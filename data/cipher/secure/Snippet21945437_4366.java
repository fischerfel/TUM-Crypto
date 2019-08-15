File pubKeyFile = new File(keyFileName);
    DataInputStream inputStream;
    byte[] signature = null;
    try {
        inputStream = new DataInputStream(new FileInputStream(pubKeyFile));
        byte[] pubKeyBytes = new byte[(int)pubKeyFile.length()];
        inputStream.readFully(pubKeyBytes);
        inputStream.close();

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubKeyBytes);
        RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(pubSpec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        signature = cipher.doFinal(secretKey.getBytes());
    } catch (Exception e) {
        e.printStackTrace();
    }
    return signature;
