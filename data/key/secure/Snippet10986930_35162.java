public String Decrypt(String strText)
{
    try
    {

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] test = decoder.decodeBuffer(strText);

        SecretKeySpec objKey = new SecretKeySpec(bytKey, "AES");
        Cipher objCipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        objCipher.init(Cipher.DECRYPT_MODE, objKey);

        byte[] bytValue = objCipher.doFinal(test);

        return new String(bytValue);
    }
    catch (Exception exc)
    {
        exc.printStackTrace();
    }

    return "";

}
