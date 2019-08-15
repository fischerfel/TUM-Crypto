        KeyStore ks = KeyStore.getInstance("jks");
        FileInputStream fis = new FileInputStream(ksFile);
        ks.load(fis, "testing".toCharArray());

        PrivateKey privateKey = (PrivateKey) ks.getKey("keys", "1234".toCharArray());

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] kekBytes = cipher.doFinal(Base64.decodeBase64(encKey.getBytes("UTF-8")));

        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[8];
        random.nextBytes(iv);

        SecretKey key = new SecretKeySpec(kekBytes, "DESede");
        Cipher cipher1 = Cipher.getInstance("DESede/CBC/NoPadding");
        cipher1.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] out = cipher1.doFinal(Base64.decodeBase64(data.getBytes("UTF-8")));

        System.out.println("Data Length: " + out.length);
        String result = new String(out, "UTF-8");
