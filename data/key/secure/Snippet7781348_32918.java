SecretKeySpec key = new SecretKeySpec(salt.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    String result = new String(cipher.doFinal(message.getBytes()));
    System.out.println("Encrypted:" + result);
