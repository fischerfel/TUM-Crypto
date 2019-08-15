public static byte[] encrypt(byte[] plainData, int offset, int length) throws Exception 
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
    cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
    return cipher.doFinal(plainData, offset, length);
}
