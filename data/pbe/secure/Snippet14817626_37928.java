        BASE64Decoder dec = new BASE64Decoder();
    byte[] salt = null;
    try {
        salt = dec.decodeBuffer(saltStr);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    Security.insertProviderAt(new BouncyCastleProvider(), 1);
    String alg = "PBEWITHSHA256AND256BITAES-CBC-BC";
    int derivedKeyLength = 256;
    int iterations = 20000;
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations,
            derivedKeyLength);
    try {
        SecretKeyFactory f = SecretKeyFactory.getInstance(alg);
        byte[] result = f.generateSecret(spec).getEncoded();
        BASE64Encoder endecoder = new BASE64Encoder();
        System.out.println(endecoder.encode(result));
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (InvalidKeySpecException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
