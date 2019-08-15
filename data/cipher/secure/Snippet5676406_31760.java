PrivateKey privKey = readPrivateKey(); // reads the private key 
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] encryptedBytes = Base64.decodeBase64(encryptedText.getBytes("UTF-32"));
        encryptedBytes = reverse(b); // reverse the bytes 
        byte[] decrypted = cipher.doFinal(encryptedBytes);
        return new String(decrypted);
