public static void main(String[] args) {

    SecretKey k1 = generateDESkey();
    SecretKey k2 = generateDESkey();

    String firstEncryption = desEncryption("plaintext", k1);
    String decryption = desDecryption(firstEncryption, k2);
    String secondEncryption = desEncryption(decryption, k1);

}

public static SecretKey generateDESkey() {
    KeyGenerator keyGen = null;
    try {
        keyGen = KeyGenerator.getInstance("DES");
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(Test.class
                .getName()).log(Level.SEVERE, null, ex);
    }
    keyGen.init(56); // key length 56
    SecretKey secretKey = keyGen.generateKey();
    return secretKey;
}

public static String desEncryption(String strToEncrypt, SecretKey desKey) {
    try {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, desKey);
        String encryptedString = Base64.encode(cipher.doFinal(strToEncrypt.getBytes()));
        return encryptedString;


    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(Test.class
                .getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchPaddingException ex) {
        Logger.getLogger(Test.class
                .getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalBlockSizeException ex) {
        Logger.getLogger(Test.class
                .getName()).log(Level.SEVERE, null, ex);
    } catch (BadPaddingException ex) {
        Logger.getLogger(Test.class
                .getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(Test.class
                .getName()).log(Level.SEVERE, null, ex);
    }
    return null;
}

public static String desDecryption(String strToDecrypt, SecretKey desKey) {
    try {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, desKey);
        String decryptedString = new String(cipher.doFinal(Base64.decode(strToDecrypt)));
        return decryptedString;


    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(Test.class
                .getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchPaddingException ex) {
        Logger.getLogger(Test.class
                .getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(Test.class
                .getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalBlockSizeException ex) {
        Logger.getLogger(Test.class
                .getName()).log(Level.SEVERE, null, ex);
    } catch (BadPaddingException ex) {
        Logger.getLogger(Test.class
                .getName()).log(Level.SEVERE, null, ex);
    } catch (Base64DecodingException ex) {
        Logger.getLogger(Test.class
                .getName()).log(Level.SEVERE, null, ex);
    }
    return null;
}
