public static String encrypt(String data) throws Exception {

        SecretKeySpec key = generateKey();
        final Cipher c = Cipher.getInstance("AES/EAX/NoPadding", "BC");

        c.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] encVal = c.doFinal(data.getBytes("UTF8"));       

        String encryptedValue = Hex.toHexString(encVal);
        return encryptedValue;
    }

    public static String decrypt(String encryptedData) throws Exception {

        Key key = generateKey();
        final Cipher c = Cipher.getInstance("AES/EAX/NoPadding", "BC");

        c.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] ba = Hex.decode(encryptedData);

        byte[] encVal = c.doFinal(ba);  

        return new String (encVal);

    }
