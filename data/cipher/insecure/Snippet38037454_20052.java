private static final byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };

public static String encrypt(String Data) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance("AES");
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encVal = c.doFinal(Data.getBytes());
    String encryptedValue = new BASE64Encoder().encode(encVal);
    return encryptedValue;
}

public static String decrypt(String encryptedData) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance("AES");
    c.init(Cipher.DECRYPT_MODE, key);
    byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
    byte[] decValue = c.doFinal(decordedValue);
    String decryptedValue = new String(decValue);
    return decryptedValue;
}

public static Key generateKey() throws Exception {
    Key key = new SecretKeySpec(keyValue, "AES");
    return key;
}

public static void main(String[] args) {
    try {
        System.out.println(AES.encrypt("test"));
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
