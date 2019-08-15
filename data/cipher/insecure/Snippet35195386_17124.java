private static final String ALGO = "AES";
    private static final byte[] keyValue = 
            new byte[] { 'a', '/', '5', '0', '0', '2', '*', 'l', '+', 'O', '&','@', 'b', '~', '_', '$' };

    //For encryption
    public String encrypt(String Data) throws Exception {
        String encryptedValue ="";
        try{
        SecretKeySpec key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, (java.security.Key) key);
        byte[] encVal = c.doFinal(Data.getBytes());
        encryptedValue = new BASE64Encoder().encode(encVal);
            return encryptedValue;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedValue;
    }


 //Generate Secret Key
    private static 




 generateKey() throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }
