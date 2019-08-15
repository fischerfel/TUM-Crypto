        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, password);
        String encrypedStr = base64encoder.encode(cipher.doFinal(cleartext));
