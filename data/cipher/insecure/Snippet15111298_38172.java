    String key = "MrSShZqHM6dtVNdX";
    String message = "NzZiNGM3ZjIyNjM5ZWM3M2YxMGM5NjgzZDQzZDA3ZTQ=";
    String charsetName = "UTF-8";
    String algo = "AES";

    // decode message
    byte[] decodeBase64 = Base64.decodeBase64(message.getBytes(charsetName));
    System.out.println("decoded message: " + new String(decodeBase64));

    // prepare the key
    SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(charsetName), algo);

    // aes 128 decipher
    Cipher cipher = Cipher.getInstance(algo);
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
    byte[] doFinal = cipher.doFinal(Hex.decodeHex(new String(decodeBase64).toCharArray()));
    System.out.println("done with: " + new String(doFinal));
