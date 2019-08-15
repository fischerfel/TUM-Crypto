public String decryptRSA(String text)
{
    try {
        final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] dec = Base64.decodeBase64(text);
        byte[] utf8 = cipher.doFinal(dec);

        return Base64.encodeBase64String(utf8);
    }
    catch (Exception ex) {
        ex.printStackTrace();
    }

    return null;
}
