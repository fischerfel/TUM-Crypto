        byte[] byteDataToEncrypt = inString.getBytes();
        final byte[] keyBytes = webSite.encryptionPassword().getBytes("ASCII");
        final byte[] ivBytes = webSite.encryptionPassword().getBytes("ASCII");
        final SecretKey key = new SecretKeySpec(keyBytes, "AES");
        final IvParameterSpec iv = new IvParameterSpec(ivBytes);
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] byteCipherText = cipher.doFinal(byteDataToEncrypt);
        byte[] aesEncryptedBase64encoded = Base64.encodeBase64(byteCipherText);
            String crypt = "@" + new String(aesEncryptedBase64encoded);
