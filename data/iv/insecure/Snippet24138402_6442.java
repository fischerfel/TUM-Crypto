private final static String ENCRYPTION_ALGORITHM = "AES";
private final static String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
private final static String ENCODING = "UTF-8";
private final static String KEY_STRING = "C0BAE23DF8B51807B3E17D21925FADF273A70181E1D81B8EDE6C76A5C1F1716E";

public static String encryptData(String plainText) {

    String keyString = KEY_STRING;
    String encryptedValue = null;

    byte[] keyValue = DatatypeConverter.parseHexBinary(keyString);
    Key key = new SecretKeySpec(keyValue, ENCRYPTION_ALGORITHM);
    Cipher cipher;
    byte[] encVal;

    try {
        cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[64]));
        encVal = cipher.doFinal(plainText.getBytes());
        encryptedValue = (new BASE64Encoder()).encode(encVal);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
        System.out.println("Error in EncryptData.encryptData(): " + e.getMessage());
        encryptedValue = null;
    }


    return encryptedValue;
}
