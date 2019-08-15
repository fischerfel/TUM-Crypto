    String algorithm = "DES";
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);

    byte[] encBytes = "12345678".getBytes("UTF8");
    byte[] decBytes = "56781234".getBytes("UTF8");

    DESKeySpec keySpecEncrypt = new DESKeySpec(encBytes);
    DESKeySpec keySpecDecrypt = new DESKeySpec(decBytes);


    SecretKey keyEncrypt = keyFactory.generateSecret(keySpecEncrypt);
    SecretKey keyDecrypt = keyFactory.generateSecret(keySpecDecrypt);

    Cipher cipherEncrypt = Cipher.getInstance(algorithm);
    Cipher cipherDecrypt = Cipher.getInstance(algorithm);

    String input = "john doe";

    cipherEncrypt.init(Cipher.ENCRYPT_MODE, keyEncrypt);
    byte[] inputBytes = cipherEncrypt.doFinal(input.getBytes());
    System.out.println("inputBytes: " + new String(inputBytes));

    cipherDecrypt.init(Cipher.DECRYPT_MODE, keyDecrypt);
    byte[] outputBytes = cipherDecrypt.doFinal(inputBytes);
    System.out.println("outputBytes: " + new String(outputBytes));
