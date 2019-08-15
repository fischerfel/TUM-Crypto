    SecretKeySpec sks = null;
    try {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed("Complex Key for encryption".getBytes());
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128, sr);
        sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
    } catch (Exception e) {
        Log.e(TAG, "AES secret key spec error");
    }

    // Encode the original data with AES
    byte[] encodedBytes = null;
    try {
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, sks);
        encodedBytes = c.doFinal(theTestText.getBytes());
    } catch (Exception e) {
        Log.e(TAG, "AES encryption error");
    }
    TextView tvencoded = (TextView)findViewById(R.id.textitem2);
    tvencoded.setText("[ENCODED]:\n" +
            Base64.encodeToString(encodedBytes, Base64.DEFAULT) + "\n");

    // Decode the encoded data with AES
    byte[] decodedBytes = null;
    try {
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, sks);
        decodedBytes = c.doFinal(encodedBytes);
    } catch (Exception e) {
        Log.e(TAG, "AES decryption error");
    }
    TextView tvdecoded = (TextView)findViewById(R.id.textitem3);
    tvdecoded.setText("[DECODED]:\n" + new String(decodedBytes) + "\n");
