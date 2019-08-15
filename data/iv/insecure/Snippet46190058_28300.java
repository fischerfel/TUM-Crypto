 public String decrypt(String text) throws Exception{

    //    byte[] keyBytess={some byte values};
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes= new byte[16];
        byte[] b= { some byte values };
        byte[] iv = { some byte values };


        int len= b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);

        BASE64Decoder decoder = new BASE64Decoder();
        byte [] results = cipher.doFinal(decoder.decodeBuffer(text));
        return new String(results,"UTF-8");
    }

    public String encrypt(String text)
            throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes= new byte[16];
        byte[] b= { some byte values };
        byte[] iv = { some byte values };
        int len= b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);
        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(results);
    }
