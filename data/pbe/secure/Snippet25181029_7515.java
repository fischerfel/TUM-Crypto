    KeySpec keySpec = new PBEKeySpec(encryptionPassword.toCharArray(), salt, iterations);

    SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
    AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterations);


//encryption
    Cipher encoder = Cipher.getInstance(key.getAlgorithm());
    encoder.init(Cipher.ENCRYPT_MODE, key, paramSpec);

    String str_to_encrypt = "Hello";
    byte[] enc = encoder.doFinal(str_to_encrypt.getBytes("UTF8"));

    System.out.println("encrypted = " + DatatypeConverter.printBase64Binary(enc));


output: encrypted = vjXsSX0cBNc=
