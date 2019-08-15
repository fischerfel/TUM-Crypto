    SecretKey key = new SecretKeySpec(SHARED_DECRYPTION_KEY.getBytes("UTF8"), "DESede");
    byte[] encryptedSecretBytes = Base64.decode(secret);     
    Cipher cipher = Cipher.getInstance("DESede"); // cipher is not thread safe
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] plainTextSecretBytes = (cipher.doFinal(encryptedSecretBytes));
    String decryptedSecret = Base64.encodeBytes(plainTextSecretBytes);
