    Cipher cipher = Cipher.getInstance("AES");
    SecretKeySpec keySpec = new SecretKeySpec(masterKey, "AES");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
