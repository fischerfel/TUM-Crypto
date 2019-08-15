public String decrypt(String encryptedPassword)
    {
        String plainPassword = "";
        try
        {
            SecretKeySpec key = new SecretKeySpec("hcxilkqbbhczfeultgbskdmaunivmfuo".getBytes("US-ASCII"), "AES");

            IvParameterSpec iv = new IvParameterSpec("ryojvlzmdalyglrj".getBytes("US_ASCII"));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

            cipher.init(Cipher.DECRYPT_MODE, key, iv);

            byte[] encoded = cipher.doFinal(Base64.decodeBase64(encryptedPassword.getBytes()));
            plainPassword = new String(encoded);
        }
        catch (Exception ex)
        {
            Log.d("Decryption Error", ex.toString());
        }

        return plainPassword;
    }
