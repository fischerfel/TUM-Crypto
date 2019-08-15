    byte[] descryptedData = null;
    try {
        byte[] data = new byte[(int) file.length()];
        new FileInputStream(file).read(data)

        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(con.getAssets().open("rsaPrivate.p12"), "password".toCharArray());
        pk = (PrivateKey)keystore.getKey("1", "password".toCharArray());

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, pk );
        descryptedData = cipher.doFinal(data);

    } catch (Exception e) {
        e.printStackTrace();
    }

    return new String(descryptedData);
