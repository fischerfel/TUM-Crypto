IvParameterSpec ivParameterSpec = new IvParameterSpec("randombigrandom".getBytes("UTF-8"));
        bytes = key.getBytes("UTF-8");
        keySpec = new SecretKeySpec(bytes, "AES");
        cipher = Cipher.getInstance("AES/CCM/NOPADDING", new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        ret = ByteBuffer.wrap(cipher.doFinal(Base64Utils.decode(requestBody.getEncryptedData().getBytes())));
