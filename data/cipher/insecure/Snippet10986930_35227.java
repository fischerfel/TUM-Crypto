public String Decrypt(String strText)
{
    try
    {
        // Text to decrypt
        byte[] test = strText.getBytes();

        //bytKey is the same key as Python app
        SecretKeySpec objKey = new SecretKeySpec(bytKey, "AES");
        Cipher objCipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        objCipher.init(Cipher.DECRYPT_MODE, objKey);

        // Here I got an exception >>
        byte[] bytValue = objCipher.doFinal(test);

        return new String(bytValue);
    }
    catch (Exception exc)
    {
        exc.printStackTrace();
    }

    return "";

}
