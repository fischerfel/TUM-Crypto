public String encryptRSA(String text)
{
    try {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] utf8 = text.getBytes("UTF-8");
        byte[] enc = cipher.doFinal(utf8);

        return new String(Base64.encodeBase64(enc), "UTF-8");
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
