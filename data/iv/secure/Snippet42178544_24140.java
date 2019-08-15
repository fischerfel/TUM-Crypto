public String encrypt(String messageText)
{
    try
    {
        byte[] data = messageText.getBytes("UTF-8");


        IvParameterSpec iv = new IvParameterSpec(iv_.getBytes("UTF-8"));
        SecretKeySpec key = (SecretKeySpec) generateKey();

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);


        byte[] textBytes = cipher.doFinal(messageText.getBytes("UTF-8"));
        String textString = Base64.encodeToString(textBytes, Base64.DEFAULT);

        return  textString;


    }
    catch (Exception ex)
    {
        ex.printStackTrace();
    }

    return null;
}
