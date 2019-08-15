        KeyAgreement keyAgreement = this.getSecretKeyAgreement(publicOtherUserKey, privateOwnKey);
        byte[] encodedBytes = text.getBytes();
        SecretKey secretKey = new SecretKeySpec(keyAgreement.generateSecret(), "AES");
        byte[] decodedBytes = Base64.decodeBase64(encodedBytes);
        Cipher decrypt = Cipher.getInstance("AES");
        decrypt.init(Cipher.DECRYPT_MODE, secretKey);
        textDecrypted = new String(decrypt.doFinal(decodedBytes));
