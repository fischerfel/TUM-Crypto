public static String decryptDataWithKey(String keyString, String base64String) throws Exception {

        //Algorithm
        String AES_ALGORITHM = "AES"; 

        //Key from keystring
        MessageDigest digester = MessageDigest.getInstance("MD5");
        digester.update(keyString.getBytes());
        byte[] password = digester.digest();
        Key key =  new SecretKeySpec(password, AES_ALGORITHM); // what is the equivalent of this line in javascript ?

        //Create decipher
        Cipher c = Cipher.getInstance(AES_ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);

        //Get bytes of enc data
        byte[] decodedValue = new BASE64Decoder().decodeBuffer(base64String);

        // Do decrypt
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
