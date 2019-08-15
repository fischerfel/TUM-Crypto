    String key = "Bar12345Bar12345"; // 128 bit key
    String initVector = "RandomInitVector"; // 16 bytes IV
    IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
    System.out.println(iv.getIV());
    SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
    byte[] ciphertext = cipher.doFinal(plainText);
