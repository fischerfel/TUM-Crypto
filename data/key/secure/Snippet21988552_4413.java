private Cipher encrypt(byte[] input)
{
    try
    {
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

        // encryption pass
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
        FileOutputStream fs = new FileOutputStream(savedScoresFileName);
        fs.write(cipherText);

        return cipher;
    }
    catch (Exception e)
    {
        Log.e("encrtypt", "Exception", e);
    }

    return null;
}

private String decrypt()
{
    try
    {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        byte[] cipherText = new byte[32];

        FileInputStream fl = new FileInputStream(savedScoresFileName);
        fl.read(cipherText);

        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainText = new byte[cipher.getOutputSize(32)];
        int ptLength = cipher.update(cipherText, 0, 32, plainText, 0);
        ptLength += cipher.doFinal(plainText, ptLength);

        return new String(plainText).substring(0, ptLength);
    }
    catch (Exception e)
    {
        Log.e("decrypt", "Exception", e);
    }

    return null;
}
