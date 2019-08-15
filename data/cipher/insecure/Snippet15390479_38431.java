    byte[] keyBytes = plainTextKey.getBytes("US-ASCII");
    SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(plainText.getBytes("US-ASCII"));
