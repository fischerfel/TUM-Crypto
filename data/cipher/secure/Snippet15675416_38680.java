Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
        byte[] decryptval = Base64.decodeBase64(encryptedData);
        byte[] decrypted = cipher.doFinal(decryptval);
        return new String(decrypted);
