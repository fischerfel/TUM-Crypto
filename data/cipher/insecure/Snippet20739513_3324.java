public static byte[] encrypt(String input, String salt) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    SecretKeySpec key = new SecretKeySpec(salt.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return cipher.doFinal(input.getBytes());
}
