    String message = "Hello world 123456";

    // Creating Key. Key of size = 128
    byte [] raw ={-49, -44, 51, -114, 58, 79, 83, -38, 107, 64, 67, -108, -52, 109, 85, 77};
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

    // Encript
    byte[] encrypted = cipher.doFinal(message.getBytes());
    System.out.println("encripted: " + Arrays.toString(encrypted));
    System.out.println("encripted: "+ asHex(encrypted));

    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] decript = cipher.doFinal(encrypted, 0, 32);
