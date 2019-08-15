        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = password.getBytes("UTF-8");
        digest.update(keyBytes);
        keyBytes = Arrays.copyOf(digest.digest(), 16);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));

        return new String(decryptedBytes);
