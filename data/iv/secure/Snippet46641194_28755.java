public static String encrypt(String value, String secret, String initVector) {
    try {
        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes());
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return Base64.encodeBase64String(encrypted);
    } catch (Exception e) {
        throw new RuntimeException(e.getMessage(), e);
    }
}

public static String decrypt(String value, String secret, String initVector) {
    try {
        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes());
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
        byte[] decrypted = cipher.doFinal(Base64.decodeBase64(str));
        return new String(decrypted);
    } catch (Exception e) {
        throw new RuntimeException(e.getMessage(), e);
    }
}
