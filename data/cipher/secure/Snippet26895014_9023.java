    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);
    byte[] stringBytes = pass.getBytes();
    byte[] raw = cipher.doFinal(stringBytes);
    return Base64.encodeBase64String(raw);
