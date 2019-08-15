private static final String ALGO = "AES";



public static String encrypt(String Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
     //   String encryptedValue = new BASE64Encoder().encode(encVal);

        byte[] decoded = Base64.encodeBase64(encVal);

        return (new String(decoded, "UTF-8") + "\n");
    }

public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue =Base64.decodeBase64(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);

        return decryptedValue;

    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(Constant.keyValue, ALGO);
        return key;
    }
