public static String encrypt(String text, String keyPhrase) throws Exception {
        byte[] salt = { 0x49, 0x76, 0x61, 0x6e, 0x20, 0x4d, 0x65, 0x64, 0x76, 0x65, 0x64, 0x65, 0x76 };
        byte[] data = text.getBytes("UTF-16LE");
        PBEKeySpec spec = new PBEKeySpec(keyPhrase.toCharArray(), salt, 1);

        SecretKey secret = new SecretKeySpec(keyPhrase.getBytes("UTF-16LE"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] ciphertext = cipher.doFinal(data);
        return Base64.encodeBase64String(ciphertext);
    }
