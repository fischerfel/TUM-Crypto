public static SecretKeySpec createSecretKey(String mKey) {
    SecretKeySpec secretKey = null;
    MessageDigest sha = null;
    try {
        byte[] key = mKey.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit
        secretKey = new SecretKeySpec(key, "AES");
    } catch (NoSuchAlgorithmException ex) {
        System.out.println("- createSecretKey > NoSuchAlgorithmException:" + ex.getMessage());
    } catch (UnsupportedEncodingException ex) {
        System.out.println("- createSecretKey > UnsupportedEncodingException:" + ex.getMessage());
    } catch (Exception ex) {
        System.out.println("- createSecretKey > Exception:" + ex.getMessage());
    }
    return secretKey;
}

public static String encryptAES(String stringToEncrypt, SecretKeySpec secretKey) {        //Rijndael
    String encryptedString = null;
    try {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        encryptedString = Base64.encodeBase64String(cipher.doFinal(stringToEncrypt.getBytes("UTF-8")));
    } catch (Exception ex) {
        System.out.println("- encryptAES > Exception: " + ex.getMessage());
    }
    return encryptedString;
}
