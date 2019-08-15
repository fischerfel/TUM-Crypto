public static String encryptString(String string, String key){
    try {
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(string.getBytes());
        return Base64.encodeToString(encrypted, Base64.URL_SAFE);
    } catch(Exception e){
        return e.getMessage();
    }
}

private static String decryptString(String string, String key){
    try {
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(string.getBytes());
        return Base64.decode(decrypted, Base64.DEFAULT ).toString();

    } catch(Exception e){
        return null;
    }
}
