public static String aes_encrypt (String text, String key) 
{
    SecretKey skey = new SecretKeySpec(key.getBytes(), "AES"); 
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
    cipher.init(Cipher.ENCRYPT_MODE, skey);

    return new String((cipher.doFinal(text.getBytes())));

}
