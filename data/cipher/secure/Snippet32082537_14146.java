    Key key = new SecretKeySpec(yourKeyValue, "AES");
    Cipher enc = Cipher.getInstance("AES/CTR/NoPadding");
    enc.init(Cipher.ENCRYPT_MODE, key);
    // Get the IV that was generated
    byte[] iv = enc.getIV();
    // Encrypt your data
    ...
    Cipher dec = Cipher.getInstance("AES/CTR/NoPadding");
    dec.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
    // Decrypt your data
    ...
