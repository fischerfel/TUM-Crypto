public class Encryption {

public void doStuff() {

    String plaintext = "abc";

    SecretKey k1 = generateDESkey();
    SecretKey k2 = generateDESkey();


    String firstEncryption = desEncryption(plaintext, k1);
    String decryption = desDecryption(firstEncryption, k2);
    String secondEncryption = desEncryption(decryption, k1);

    System.out.println(firstEncryption);
    System.out.println(decryption);
    System.out.println(secondEncryption);
}

public static SecretKey generateDESkey() {
    KeyGenerator keyGen = null;
    try {
        keyGen = KeyGenerator.getInstance("DESede");
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(Test.class
                .getName()).log(Level.SEVERE, null, ex);
    }
    try {
        assert keyGen != null;
        keyGen.init(112); // key length 56
        return keyGen.generateKey();
    } catch (NullPointerException ex){
        return null;
    }
}


public static String desEncryption(String strToEncrypt, SecretKey desKey) {
    try {
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, desKey);
        return Base64.encode(cipher.doFinal(strToEncrypt.getBytes()));
    } catch (NoSuchAlgorithmException | NoSuchPaddingException |
            IllegalBlockSizeException | BadPaddingException |
            InvalidKeyException ex) {
        Logger.getLogger(Test.class
                .getName()).log(Level.SEVERE, null, ex);
    }
    return null;
}


public static String desDecryption(String strToDecrypt, SecretKey desKey) {
    try {
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, desKey);
        return new String(cipher.doFinal(Base64.decode(strToDecrypt)));

    } catch (NoSuchAlgorithmException |  BadPaddingException | IllegalBlockSizeException
            | InvalidKeyException | NoSuchPaddingException ex) {
        Logger.getLogger(Test.class
                .getName()).log(Level.SEVERE, null, ex);
    }
    return null;
}

}
