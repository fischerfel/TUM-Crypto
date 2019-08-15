private static SecretKeySpec secret;
private static String seed;
private static String text;
private static String salt = "Salt123";
private static int pswdIterations = 65536;
private static int keySize = 256;

/**
 * 
 * @param mySeed
 */
public static void setSeed(String mySeed) {

    try {
        byte[] saltBytes = salt.getBytes("UTF-8");
        PBEKeySpec spec = new PBEKeySpec(mySeed.toCharArray(), saltBytes,
                pswdIterations, keySize);
        SecretKeyFactory factory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        SecretKey secretKey = factory.generateSecret(spec);
        secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    }

}

public static String getEncryptedStringFor(String text) {
    try {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] encryptedData = cipher.doFinal(text.getBytes("UTF-8"));
        return new String(Base64.encodeBase64(encryptedData));

    } catch (Exception e) {
        System.out.println("Error while encrypting: " + e.toString());
    }
    return null;
}

public static String getDecryptedStringFor(String text) {
    try {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        return (new String(cipher.doFinal(Base64
                .decodeBase64(text.getBytes("UTF-8")))));
    } catch (Exception e) {
        System.out.println("Error while decrypting: " + e.toString());
    }
    return null;
}
