        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKey key = generateKey(passphrase);
        byte[] ivBytes = DatatypeConverter.parseBase64Binary(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));
        byte[] encBytes = cipher.doFinal(strToEncrypt.getBytes("UTF-8"));
        // concat iv + encripted bytes
        byte[] concat = new byte[ivBytes.length + encBytes.length];
        System.arraycopy(ivBytes, 0, concat, 0, ivBytes.length);
        System.arraycopy(encBytes, 0, concat, ivBytes.length, encBytes.length);
        encryptedStr = DatatypeConverter.printBase64Binary(concat);
