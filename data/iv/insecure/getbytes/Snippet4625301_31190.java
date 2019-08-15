KeySpec ks = new DESKeySpec("hcxilkqbbhczfeultgbskdmaunivmfuo".getBytes("UTF-8"));
            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(ks);

        String ivString = "ryojvlzmdalyglrj";
        byte[] ivByte = ivString.getBytes("UTF-8");
        IvParameterSpec iv = new IvParameterSpec(ivByte);
        //RC5ParameterSpec iv = new RC5ParameterSpec(ivByte);

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] encoded = cipher.doFinal(Base64.decodeBase64("iNtaFme3B/e6DppNSp9QLg=="));

        Log.d("Decoded Password", encoded.toString());
