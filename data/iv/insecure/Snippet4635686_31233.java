public String encrypt(String plainPasword)
    {
        String password = "";
        try
        {
            SecretKeySpec key = new SecretKeySpec("hcxilkqbbhczfeultgbskdmaunivmfuo".getBytes("US-ASCII"), "AES");

            IvParameterSpec iv = new IvParameterSpec("ryojvlzmdalyglrj".getBytes("US_ASCII"));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            byte[] encoded = cipher.doFinal(plainPasword.getBytes());
            password = new String(encoded);


        }
        catch (Exception ex)
        {
            Log.d("Encryption Error", ex.toString());
        }
        return password;
    }
