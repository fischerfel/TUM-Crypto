public static void main(String[] args) {
       String key = "1234567890ABCDEF";
        try {
            byte[] encrypt = encrypt("hello word",key);
            System.out.println(new String(encrypt));
            String decrypt = decrypt(encrypt, key);
            System.out.println(decrypt);
        } catch (Exception ex) {

        }

    }

   public static byte[] encrypt(String message, String key1) throws Exception {

        SecretKeySpec key = new SecretKeySpec(key1.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(message.getBytes());
    }

    public static String decrypt(byte[] message, String key1) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(key1.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedByte = cipher.doFinal(message);
        String decryptedText = new String(decryptedByte);
        return decryptedText;
    }
