private static final String ALGO = "AES";
private static final byte[] keyValue = new byte[] { '1', '1', '1', '1', '1', '1', '1', '1', '1','1', '1', '1','1','1','1','1' };

public static String encrypt(String Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
     //   String encryptedValue = new BASE64Encoder().encode(encVal);

        byte[] decoded = Base64.encodeBase64(encVal);

        return (new String(decoded, "UTF-8") + "\n");
    }

private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }
