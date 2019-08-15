    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithMD5And256AES-CBC");
    KeySpec spec = new PBEKeySpec("Password".toCharArray(), null, 2048, 256);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = cipher.getParameters();
    byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
