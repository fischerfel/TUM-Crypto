public static String encrypt(String plainPasword)
    {
            String password = "";
            try
            {
                SecretKeySpec key = new SecretKeySpec("hcxilkqbbhczfeultgbskdmaunivmfuo".getBytes("US-ASCII"), "AES");
                IvParameterSpec iv = new IvParameterSpec("ryojvlzmdalyglrj".getBytes("US-ASCII"));

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

                cipher.init(Cipher.ENCRYPT_MODE, key, iv);

                byte[] encoded = cipher.doFinal(plainPasword.getBytes());
                password = new String(Base64.encodeBase64(encoded));

            }
            catch (Exception ex)
            {
                System.err.println("Encryption Exception: " + ex.toString());
            }
            return password;
    }
