private void exampleMethod(){
    String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoApIBIna77xq4j+M2RmyIhsB++41NHcY4KIPfX4VP4ADnkO+7ejbs4le/twrPtYGESVPF9czSMB5bzmTBZtq0jC8oT/6wiDIBlSuzo4fBrGociBIuaOjyG/j3ZhpcWpWPXuzER+ehuQ+8hZkMuJdK9IodqPR+5jmCef4rXoKObwS02LYQ1co5dEmtZVQRmmeYaVnWibd/s1d4KKGvSzXap3YBTf8peH5UGIQrLOTqvX0bo34xFxmj5U0H3xudnnwuVAlQlj9KiHPPABuwNtm1buRKJb5HZhSCveyT/2YAOmQqGrVN/nALtlZyTDZNs//Vp1zb9exSuG0t5xFc+pn4QIDAQAB";
    String privKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCgCkgEidrvvGriP4zZGbIiGwH77jU0dxjgog99fhU/gAOeQ77t6NuziV7+3Cs+1gYRJU8X1zNIwHlvOZMFm2rSMLyhP/rCIMgGVK7Ojh8GsahyIEi5o6PIb+PdmGlxalY9e7MRH56G5D7yFmQy4l0r0ih2o9H7mOYJ5/itego5vBLTYthDVyjl0Sa1lVBGaZ5hpWdaJt3+zV3gooa9LNdqndgFN/yl4flQYhCss5Oq9fRujfjEXGaPlTQffG52efC5UCVCWP0qIc88AG7A22bVu5EolvkdmFIK97JP/ZgA6ZCoatU3+cAu2VnJMNk2z/9WnXNv17FK4bS3nEVz6mfhAgMBAAECggEBAIax4IchV0jqdbLR9cNK4yfdP0A/7jun+SImg48FLPDy1xi+v9UQZMioV3F88FDEZPrNQdI45wrWI94+wMS5V6BsMHYumOgGGxNo9m8WInrJz5GuJkdHuLMbqNZ6TlSMQOUiVUWWLSAuveOWgOJqriwRhsjDfBmbSBESUbP/wNdxX42RJudKJbVUV07urjFc3VAUrX+Sbj9KMZRe10pOzes7WKq9NMyQuitLcwCPzs2pQoXW1LdZYrVSi6MOqE1WHSL6VAgPx3cHYl7yznhspLmvDgnKTwnVbRo3BBwkxNbpvZXzgJBLEPLUtLqwNLKkU0aSF9MT5TABx4tfCaRQIiECgYEA70980I9mK74tjXlFrMKaLGigjHBss+Q/b7cRAtlQAhVOn0FCqz4Fc4iBri0iPekVIZ09lRehacEstTR8JBImMW2mqGyMwBbPaqQOf6xZ0pIoYb0ODAIjUNTWoBEr72+ko5HjaoQbxeb2QGUhMe/t3M1CMsrETEQTdA+qNP5C61UCgYEAqzOL7sSsNfTYbAF156qPPqx1IyXNqu0wbKa/zufCxGlFJDkaYoYIECKdLbpI2fqJsENqpZHgOnT0+LbqhFn07NIe1zT/zf0rh7w5fqxqy3Srs4+Mj6HwTIC7QpeXjiHxQuVrfi2W2ZatjQi8froxtEj3mpYKHsl0Ia89JSczQl0CgYAqHPXdCe8z8XK4u8esIE7bU8o1DK/EdH1JXpDqzG1NAIzmb6iY1ABHlZUknqKw/GyQjshAjXkFUE5a0RKrkloQRriWWQvn3dvAa4B1rVHdQYVDte5b5KBsYBgo8PynVSFG+6xmmTr996gMKv/NduiH+8MThyVGOpCl0v/j9X63RQKBgEmOhir6eXtdTbdqETyOPamR82o8jddIvauRIYxGa5p0GG7t0fZO3BwCo0HIbhCp4orHDIVC3fJ/2dkazjw7Yk52ISYZ8WaRxig1qQZSEjiEUll97ciwrUxRayO7ejRpRP2XEM5PzCaE5OBZxpM0cLKjPy8+E+8SY0Etx7m01ANJAoGBALUubgeKx1fut80YHLDmxOiTg9olFJi83Lj1TPQ0fRCXdJX6pHCSypBScoXuJYVwuIavHhTf8DPQ6OONq/V3DXKsGLydK/2E5yg+bz3qYfYslb3vDkZovNJDmfoyR0XakWbUTotntUQqodLk8Q9klHKp6oy+MkGY57R5OhIZBGPa";

    String message = "Why does this not work in Android?";
    String encryptedMessage = getUrlEncodedCipherText(message, pubKey);

    try {
        byte[] base64Decoded = Base64.decodeBase64(encryptedMessage.getBytes(Charset.forName("UTF-8")));
        String decryptedMessage = decrypt(base64Decoded, loadPrivateKey(privKey));
        System.out.println("decryptedMessage: " + decryptedMessage);
        /**
         * This works! Ciphertext always comes out different, as expected, and decodes successfully.
         */
    }
    catch (Exception e){
        e.printStackTrace();
    }
}

public static String getUrlEncodedCipherText(String plainText, String pubKey){
    try {
        final PublicKey publicKey = loadPublicKey(pubKey);
        final byte[] cipherBytes = encrypt(plainText, publicKey);
        String cipherText = base64Encode(cipherBytes);
        String urlEncodedCipherText = urlEncode(cipherText);
        return urlEncodedCipherText;
    }
    catch (Exception e){
        e.printStackTrace();
        return null;
    }
}

public static final String ALGORITHM = "RSA";

public static PublicKey loadPublicKey(String stored) throws GeneralSecurityException {
    String pubKey = stored.replace(BEGIN_PUBLIC_KEY, "");
    pubKey = pubKey.replace(END_PUBLIC_KEY, "");

    byte[] data = Base64.decodeBase64(pubKey);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
    KeyFactory fact = KeyFactory.getInstance(ALGORITHM);
    PublicKey pub = fact.generatePublic(spec);
    return pub;
}

public static byte[] encrypt(String text, PublicKey key) {
    byte[] cipherText = null;
    try {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherText = cipher.doFinal(text.getBytes());
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    return cipherText;
}

public static String decrypt(byte[] encrypted, PrivateKey key) {
    byte[] decryptedText = null;
    try {
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        decryptedText = cipher.doFinal(encrypted);
    }
    catch (Exception e) {
        e.printStackTrace();
        return null;
    }
    return new String(decryptedText);
}

public static String base64Encode(byte[] cipherBytes){
    byte[] base64Cipher = Base64.encodeBase64(cipherBytes);
    return new String(base64Cipher);
}

public static String urlEncode(String text){
    return text.replace("+", "-").replace("/", "_").replace("=", ",");
}
