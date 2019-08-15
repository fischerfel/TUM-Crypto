public String decodeInputString(String inputString) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        byte[] salt = "MyKey".getBytes();
        SecretKey secretKey = new SecretKeySpec(salt, 0, 16, "AES");
        byte[] encryptedTextByte = Base64.decode(inputString);
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        return decryptedText;

}
