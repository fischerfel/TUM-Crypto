public static String decrypt(String strToDecrypt)
    {

        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            final SecretKeySpec secretKey = new SecretKeySpec(AppConstants.AESEncryptionKey.getBytes("UTF8"), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey,new IvParameterSpec(new byte[16])); //new IvParameterSpec(new byte[16])
            byte base64Data[] = Base64.encode(strToDecrypt.getBytes(), Base64.DEFAULT);
            @SuppressWarnings("unused")
            String s = base64Data.toString();
            byte decBytes[] = cipher.doFinal(base64Data);
            String decStr = new String(decBytes);
            return decStr;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
