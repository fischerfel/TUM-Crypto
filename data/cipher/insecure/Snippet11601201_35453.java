   Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Hex.decodeHex(encryptionKey.toCharArray()), "AES"));
    decrypted = new String(cipher.doFinal(Hex.decodeHex(enc.toCharArray())));
