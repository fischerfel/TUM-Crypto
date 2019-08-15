byte[] input = "Hello from Android!".getBytes("UTF-8");
        Cipher cipher = Cipher.getInstance("RSA", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);                

        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
        String encodedData = Base64.encodeToString(cipherText, messageCount);
        System.out.println(new String(encodedData));
        System.out.println(ctLength);
