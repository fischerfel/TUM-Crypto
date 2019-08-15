            KeyAgreement keyAgreement = this.getSecretKeyAgreement(publicOtherUserKey, privateOwnKey);
            SecretKey secretKey = new SecretKeySpec(keyAgreement.generateSecret(), "AES");
            Cipher aesCipher = null;
            aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] byteDataToEncrypt = text.getBytes();
            byte[] byteCipherText = aesCipher.doFinal(byteDataToEncrypt);
            byte[] encodedBytes = Base64.encodeBase64(byteCipherText);
            textEncrypted = new String(encodedBytes);
