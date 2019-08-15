public static String encrypt(String plainText, Character fileType) {
    String encryptedString = "";
    if (!isValidFile(fileType))
        throw new IllegalArgumentException("Invalid fileType.");
    else 
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");  
            SecretKeySpec secretKey = new SecretKeySpec(keys.get(fileType), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedString = Base64.encode(cipher.doFinal((plainText).getBytes()));

        }   catch (Exception e) {
            e.printStackTrace();
        }
    return encryptedString;
}

private static boolean isValidFile(Character type) {
    return (type == CAMPAIGN || type == EVENT || type == TRANSACTION || type == ACCOUNT || type == EXEC || type == PASSWORD);
}

public static String decrypt(String cipherText, Character fileType) {
    String decryptedString = "";

    if (!isValidFile(fileType))
        throw new IllegalArgumentException("Invalid fileType.");
    else 
        try {
            com.sun.org.apache.xml.internal.security.Init.init();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            SecretKeySpec secretKey = new SecretKeySpec(keys.get(fileType), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decryptedString = new String(cipher.doFinal(Base64.decode(cipherText)));

        }   catch (Exception e) {
            e.printStackTrace();
        }
    return decryptedString;
}
