    SecretKeySpec sks = null;
    try {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed("exSeed".getBytes());
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128, sr);
        sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
    } catch (Exception e) {
        Log.e(TAG, "AES secret key spec error");
    }

    byte[] decodedBytes = null;
    try {
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, sks);
        decodedBytes = c.doFinal(key);
    } catch (Exception e) {
        Log.e(TAG, "AES decryption error");
    }
    String decoded = new String(decodedBytes);
