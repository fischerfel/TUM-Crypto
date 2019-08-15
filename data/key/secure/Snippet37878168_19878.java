public static byte[] encrypt(String plainText, String encryptionKey) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
    MessageDigest sha = MessageDigest.getInstance("SHA-1");
    byte [] hashedPassword = sha.digest(encryptionKey.getBytes("UTF-8"));
    hashedPassword = Arrays.copyOf(hashedPassword, 16);
    SecretKeySpec key = new SecretKeySpec(hashedPassword, "AES");
    cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
    return cipher.doFinal(plainText.getBytes("UTF-8"));
}
