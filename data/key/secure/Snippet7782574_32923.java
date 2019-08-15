        SecretKeySpec key = new SecretKeySpec(salt.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
    cipher.init(Cipher.DECRYPT_MODE, key);
    String result = new String(cipher.doFinal(message));

    System.out.println("Decrypted:" + result);
