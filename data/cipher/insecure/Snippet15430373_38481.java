    byte[] key = new byte[16];
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

    byte[] data = "hello world.....".getBytes();
    byte[] encrypted = cipher.doFinal(data);
    System.out.println("Encrypted Array : " + Arrays.toString(encrypted));
