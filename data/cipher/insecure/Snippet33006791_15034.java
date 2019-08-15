public static byte[] encodeFile(byte[] key, byte[] fileData) throws Exception
    {

        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encrypted = cipher.doFinal(fileData);

        return encrypted;
    }
