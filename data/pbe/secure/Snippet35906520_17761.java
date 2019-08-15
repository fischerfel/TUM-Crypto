        SecretKeyFactory kf = SecretKeyFactory.getInstance(PBKDF2WithHmacSHA512);
        PBEKeySpec ks = new PBEKeySpec(password.toCharArray(), salt, count, keylen);
        SecretKey pbeKey = kf.generateSecret(ks);
        System.out.println(new BASE64Encoder().encode(pbeKey.getEncoded()));
