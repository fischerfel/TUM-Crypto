    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, key, iv);
    byte[] raw = Base64.decodeBase64(encText);
    byte[] stringBytes = cipher.doFinal(raw);
    String clear_text = new String(stringBytes, "UTF8");
    return clear_text;
