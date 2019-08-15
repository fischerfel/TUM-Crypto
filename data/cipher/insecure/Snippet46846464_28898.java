public static String encrypt(String message, String key) {
        String cipherText = null;

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
            byte[] bytes = cipher.doFinal(message.getBytes("UTF-8"));

            cipherText = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return cipherText;
    }

    public static String decrypt(String encoded, String key) {
        String decryptString = null;

        try {
            byte[] bytes = Base64.decode(encoded, Base64.DEFAULT);

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
            decryptString = new String(cipher.doFinal(bytes), "UTF-8");
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return decryptString;
    }
