            String input = "TheParameterIWantToEncrypt";
        String secretID = "PublicKey12345678910";
        char[] inputChars = input.toCharArray();
        char[] pswChars = secretID.toCharArray();

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithMD5AndDES", new BouncyCastleProvider());
        KeySpec spec = new PBEKeySpec(pswChars);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] ciphertext = cipher.doFinal(input.getBytes());

        System.out.println(new String(ciphertext));
