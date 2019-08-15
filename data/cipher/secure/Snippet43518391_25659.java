final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        final SecretKeySpec key = new SecretKeySpec(aesPassword().getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(aesIv().getBytes("UTF-8")));
        byte[] decrypted = cipher.doFinal(DatatypeConverter.parseBase64Binary(message));
