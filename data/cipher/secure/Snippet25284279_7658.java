cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        Key secretKeySpecification = secretKeyData.getKey();
        cipher.init(
                Cipher.ENCRYPT_MODE,
                secretKeySpecification,
                new IvParameterSpec(secretKeyData.getIV().getBytes("UTF-8")));
        byte[] bytesdata = cipher.doFinal(data.getBytes());
     String encodedData = new BASE64Encoder().encode(bytesdata)
