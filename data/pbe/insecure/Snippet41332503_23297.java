    String keyValue = "Abcdefghijklmnop";     
    SecretKeyFactory factory =   SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(keyValue.toCharArray(), hex("dc0da04af8fee58593442bf834b30739"),
        1000, 128);

    Key key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    Cipher c = Cipher.getInstance(“AES/CBC/PKCS5Padding”);
    c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(hex("dc0da04af8fee58593442bf834b30739")));

    byte[] encVal = c.doFinal("The Quick Brown Fox Jumped over the moon".getBytes());
    String base64EncodedEncryptedData = new String(Base64.encodeBase64(encVal));
    System.out.println(base64EncodedEncryptedData);
