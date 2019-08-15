public static String decrypt(String encData) throws Exception
    {
        Key key = generateKey();

        Cipher c = Cipher.getInstance(algo + "/CBC/PKCS7Padding");

        //Cipher c = Cipher.getInstance(algo);

        IvParameterSpec ivSpec = new IvParameterSpec(ivbytes);

        c.init(Cipher.DECRYPT_MODE, key , ivSpec);

        //byte[] decValue = Base64.decode(encData , Base64.NO_PADDING);

        //byte[] decFin = Base64.decode(decValue, Base64.NO_PADDING);

        //byte[] decFinal = c.doFinal(decValue);

        byte[] decFinal = c.doFinal(encData.getBytes());

        String getAns = new String(decFinal, "UTF8");

        return getAns;
    }
