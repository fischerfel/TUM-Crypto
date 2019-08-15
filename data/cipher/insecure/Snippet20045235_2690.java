String key = "someData";

    SecretKeySpec sks = null;
    try {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed("exSeed".getBytes());
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128, sr);
        sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
    } catch (Exception e) {
    }

    byte[] encodedBytes = null;
    try {
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, sks);
        encodedBytes = c.doFinal(key.getBytes());
    } catch (Exception e) {
    }
    jTextField5.setText(Base64.encode(encodedBytes));
