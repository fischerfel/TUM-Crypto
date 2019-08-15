 byte[] data = new BASE64Decoder().decodeBuffer(cipherData);
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secretKeyUsed while encrypting);
        byte[] plainData = aesCipher.doFinal(data);
        return new String(plainData);
