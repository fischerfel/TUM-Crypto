SecureRandom r = new SecureRandom();
        byte[] ivBytes = new byte[16];
        r.nextBytes(ivBytes);

        SecretKey key = generateKey(context, keyphrase.toCharArray());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));
