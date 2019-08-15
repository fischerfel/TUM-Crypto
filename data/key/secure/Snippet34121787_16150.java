    SecretKeySpec keySpec = new SecretKeySpec(decryptedKeySpec, "AES");
    Cipher decoder = Cipher.getInstance("AES");
    decoder.init(Cipher.DECRYPT_MODE, keyspec);
    byte[] original = descipher.doFinal(message);
