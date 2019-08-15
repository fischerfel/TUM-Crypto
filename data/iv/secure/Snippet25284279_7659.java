                Key secretKeySpecification = decryptionKeyDetails.getKey();
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpecification,
                new IvParameterSpec(decryptionKeyDetails.getIV()
                        .getBytes("UTF-8")));
        byte[] bytesdata;

        byte[] tempStr = new BASE64Decoder()
                .decodeBuffer(splitedData[0]);
        bytesdata = cipher.doFinal(tempStr);
        return new String(bytesdata);
