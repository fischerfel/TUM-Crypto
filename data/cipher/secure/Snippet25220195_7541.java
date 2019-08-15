public static String Sign(byte[] text, RSAPrivateKey privateKey) throws Exception
{
    // Encode the original data with RSA private key
    byte[] encodedBytes = null;
    try {
        Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        c.init(Cipher.ENCRYPT_MODE, privateKey);
        encodedBytes = c.doFinal(text);
    } catch (Exception e) {
        Log.e("RSAHelper", "RSA encryption error");
        throw e;
    }

    return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
}
