    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWITHSHA256AND128BITAES-CBC-BC", 
            org.spongycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME);

    KeySpec spec = new PBEKeySpec("password".toCharArray(), 
            "8 bytes!", 1024, 128);

    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = cipher.getParameters();

    byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
    byte[] cipherText = cipher.doFinal("hello world".getBytes());
