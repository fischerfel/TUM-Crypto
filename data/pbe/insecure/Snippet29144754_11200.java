public static SecretKey secKey;
private static IvParameterSpec ivspec;
static {
    try {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec("i15646dont6321wanna".toCharArray(),
                "ahhalkdjfslk3205jlk3m4ljdfa85l".getBytes("UTF-8"), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        secKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        ivspec = new IvParameterSpec(iv);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException | UnsupportedEncodingException e) {
        e.printStackTrace();
    }
}

public static String encryptData(String textToEncrypt) {

    byte[] encryptedBytes = null;
    String encryptedText = "";
    try {
        byte[] byteToEncrypt = textToEncrypt.getBytes(Charset.defaultCharset());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secKey, ivspec);
        encryptedBytes = cipher.doFinal(byteToEncrypt);
        encryptedText = new String(encryptedBytes);
    } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException
            | InvalidKeyException | NoSuchPaddingException | InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    }
    return encryptedText;
}

public static String decryptData(String textToDecrypt) {

    byte[] decryptedBytes = null;
    String decryptedText = "";
    try {
        byte[] byteToDecrypt = textToDecrypt.getBytes(Charset.defaultCharset());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secKey, ivspec);
        decryptedBytes = cipher.doFinal(byteToDecrypt);
        decryptedText = new String(decryptedBytes);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
            | IllegalBlockSizeException | BadPaddingException
            | InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    }
    return decryptedText;
}
