public static String decrypt(String data) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding", "BC");

        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // Base 64 encode the encrypted data
        byte[] encryptedBytes = Base64.encode(cipher.doFinal(data.getBytes()), 0);

        return new String(encryptedBytes);

    }
