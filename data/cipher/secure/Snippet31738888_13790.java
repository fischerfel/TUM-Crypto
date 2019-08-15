String seed = "somekey";
        Key key = null;
        // 128 bit key
        byte[] byteKey = seed.substring(0, 16).getBytes("UTF-8");
        key = new SecretKeySpec(byteKey, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(
                new byte[16]));
        byte[] encValue = cipher.doFinal(pValue.getBytes());
        encryptedText = new BASE64Encoder().encode(encValue);
