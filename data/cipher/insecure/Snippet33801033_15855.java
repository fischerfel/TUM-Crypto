private String encryptAES(String text) throws Exception
{
    String key = "something-random";
    SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(text.getBytes());

    String encrypttext = Base64.encodeToString(encrypted, Base64.URL_SAFE|Base64.NO_WRAP);

    Log.v("ENCRYPTED", encrypttext); // 6sAfStQJ2zNUJLdRgXZsTA==

    return encrypttext;
}
