        Cipher c = Cipher.getInstance("AES/ECB/PKCS7Padding");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = BASE64DecoderStream.decode(encryptedData.getBytes());
        byte[] decValue = c.doFinal(decordedValue);
        decryptedValue = new String(decValue); 
