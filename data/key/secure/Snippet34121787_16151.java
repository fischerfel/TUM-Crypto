    SecretKeySpec keySpec = new SecretKeySpec(decryptedKeySpec, "AES");
    Cipher decoder = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    decoder.init(Cipher.DECRYPT_MODE, keyspec);
    byte[] original = descipher.doFinal(message);
