public static String crypt(String input, String key){
    byte[] crypted = null;
    try{
        SecretKeySpec skey = new SecretKeySpec(Base64.decodeBase64(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skey);
        crypted = cipher.doFinal(input.getBytes());
    }catch(Exception e){
    }
    return Base64.encodeBase64String(crypted);
}

public static String decrypt(String input, String key){
    byte[] output = null;
    try{
        SecretKeySpec skey = new SecretKeySpec(Base64.decodeBase64(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skey);
        output = cipher.doFinal(Base64.decodeBase64(input));
    }catch(Exception e){
    }
    return new String(output);
}
