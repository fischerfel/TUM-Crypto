    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, key, iv);
    byte[] raw = Base64.decodeBase64(encryptedString);
    byte[] stringBytes = cipher.doFinal(raw);
    String decryptedString = new String(stringBytes, "UTF8");
    return decryptedString;
