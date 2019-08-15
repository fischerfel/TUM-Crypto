public String authenticate(String base64EncodedData){

    byte[] input = Base64.decodeBase64(base64EncodedData);
    byte[] ivBytes = "1234567812345678".getBytes();


    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

    cipher.init(
        Cipher.DECRYPT_MODE,
        new SecretKeySpec("exo123exo1exo123".getBytes(), "AES"),
        new IvParameterSpec(ivBytes)
    );

    byte[] plainText = new byte[cipher.getOutputSize(input.length)];
    int plainTextLength = cipher.update(input, 0, input.length, plainText, 0);
    plainTextLength += cipher.doFinal(plainText, plainTextLength);

    return new String(plainText);  
  }
