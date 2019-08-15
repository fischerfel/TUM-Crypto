private String _decrypt2(String encryptText,String key)
{

    MessageDigest md = null;
    byte[] digestOfPassword = null;

    try
    {
        byte[] message = Base64.decode(encryptText.getBytes("UTF-16LE"), Base64.DEFAULT);

        /**
         * make md5
         */
        md = MessageDigest.getInstance("md5");
        digestOfPassword = md.digest(key.getBytes("UTF-16LE"));
        byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8; )
        {
            keyBytes[k++] = keyBytes[j++];
        }

        SecretKey secretKey = new SecretKeySpec(keyBytes, 0, 24, "DESede");
        IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] cipherText = cipher.doFinal(message);

        return new String(cipherText, "UTF-16LE");
    }
    catch (NoSuchAlgorithmException e)
    {
        e.printStackTrace();
    }
    catch (UnsupportedEncodingException e)
    {
        e.printStackTrace();
    }
    catch (InvalidKeyException e)
    {
        e.printStackTrace();
    }
    catch (InvalidAlgorithmParameterException e)
    {
        e.printStackTrace();
    }
    catch (NoSuchPaddingException e)
    {
        e.printStackTrace();
    }
    catch (BadPaddingException e)
    {
        e.printStackTrace();
    }
    catch (IllegalBlockSizeException e)
    {
        e.printStackTrace();
    }
    return "";
}
