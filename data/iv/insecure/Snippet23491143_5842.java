public static String decrypt(String strToDecrypt)
{       
    try
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding"); //AES/CBC/PKCS7Padding
        SecretKeySpec secretKey = new SecretKeySpec(AppConstants.AESEncryptionKey.getBytes("UTF8"), "AES");
        int blockSize = cipher.getBlockSize();
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[blockSize])); //new IvParameterSpec(new byte[16])
        byte decBytes[] = cipher.doFinal(Base64.decode(strToDecrypt, 0));
        // byte decBytes[] = cipher.doFinal(Base64.decodeBase64(strToDecrypt));
        String decStr = new String(decBytes);
        System.out.println("After decryption :" + decStr);
        return decStr;
    }
    catch (Exception e)
    {
        System.out.println("Exception in decryption : " + e.getMessage());
    }
    return null;
}
