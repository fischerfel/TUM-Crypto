       Cipher RSACipher= Cipher.getInstance("RSA");
       RSACipher.init(Cipher.DECRYPT_MODE, testPvtKey);
       //file.getSymmetricKey method give us the encrypted AES symmetric key from database
       byte[] decsymetricKeyBytes=RSACipher.doFinal(file.getSymetricKey());

        SecretKey symetricKey = new SecretKeySpec(decsymetricKeyBytes,
                "AES");

        Cipher AESCipher= Cipher.getInstance("AES");
        AESCipher.init(Cipher.DECRYPT_MODE, symetricKey);
        byte[] plainText = AESCipher.doFinal(file.getResourceFile());
