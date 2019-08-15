    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
