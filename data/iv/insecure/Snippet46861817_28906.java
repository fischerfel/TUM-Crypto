private String _encrypt2(String clearText,String key )
{
    try
    {
        /**
         * create md5
         */
        MessageDigest md = MessageDigest.getInstance("md5");
        byte[] digestOfPassword = md.digest(key.getBytes("UTF-16LE"));
        byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8; )
        {
            keyBytes[k++] = keyBytes[j++];
        }


        SecretKey secretKey = new SecretKeySpec(keyBytes, 0, 24, "DESede");
        IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        byte[] plainTextBytes = clearText.getBytes("UTF-16LE");
        byte[] cipherText = cipher.doFinal(plainTextBytes);

        String output = Base64.encodeToString(cipherText,Base64.DEFAULT);
        return output;
    }
    catch (Exception ex) {}
    return "";
}
