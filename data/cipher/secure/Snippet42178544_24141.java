   public String decrypt(String msgText)
    {
        try
        {
            byte[] data = msgText.getBytes("UTF-8");

            IvParameterSpec iv = new IvParameterSpec(iv_.getBytes("UTF-8"));
            SecretKeySpec skeySpec = (SecretKeySpec) generateKey();

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);


            byte[] original = cipher.doFinal(data);
            String textString = new String(original, "UTF-8");
            return textString;


        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }
