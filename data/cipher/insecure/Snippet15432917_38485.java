    Cipher cipher;
    byte[] bytes = null;

    try
    {
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, generateAESKey128b(key));
        bytes = cipher.doFinal(input.getBytes("UTF-8"));
    }
    catch (NoSuchAlgorithmException e)
    {
        e.printStackTrace();
    }
    catch (NoSuchPaddingException e)
    {
        e.printStackTrace();
    }
    catch (InvalidKeyException e)
    {
        e.printStackTrace();
    }
    catch (UnsupportedEncodingException e)
    {
        e.printStackTrace();
    }
    catch (IllegalBlockSizeException e)
    {
        e.printStackTrace();
    }
    catch (BadPaddingException e)
    {
        e.printStackTrace();
    }
