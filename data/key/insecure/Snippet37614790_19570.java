public String encrypt(String plainPwd)
{
    byte[] outputBytes = new byte[] {};
    String returnString = "";
    try
    {
        byte[] raw = "XXXXX@XXXXXX.XXX".getBytes("UTF-8");

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        outputBytes = cipher.doFinal(plainPwd.getBytes("UTF-8"));
        if (null != outputBytes)
        {
            returnString = Base64Encrypter.getInstance().encode(outputBytes);
        }
        return returnString.trim();

    }
    catch (Exception e)
    {
        System.out.println(e);
    }

    return new String(outputBytes).trim();
}

public String decrypt(String encryptedPwd)
{
    byte[] outputBytes = new byte[] {};
    try
    {
        byte[] raw = "XXXXX@XXXXXX.XXX".getBytes("UTF-8");

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] inputBytes = Base64Encrypter.getInstance().decode(encryptedPwd);
        if (null != inputBytes)
        {
            outputBytes = cipher.doFinal(inputBytes);
        }
    }
    catch (Exception e)
    {
        System.out.println(e);
    }

    return new String(outputBytes).trim();
}
