public static String aes_decrypt (String text, String key) 
{

    SecretKey skey = new SecretKeySpec(key.getBytes(), "AES"); 
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
    cipher.init(Cipher.DECRYPT_MODE, skey);

    return new String((cipher.doFinal(text.getBytes())));
}
