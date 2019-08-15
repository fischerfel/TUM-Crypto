public class AES {
public static String SALT = "8e0b86611d5922ffd57fcc053644ff6d73459b2b";
public static SecretKeySpec getKey(String myKey) {
    MessageDigest sha = null;
    byte[] key;
    try {
        key = myKey.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit
        return new SecretKeySpec(key, "AES");
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return null;
}

public static String encrypt(String strToEncrypt, String password) {
    try {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getKey(password));
        return Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
    } catch (Exception e) {
        System.out.println("Error while encrypting: " + e.toString());
    }
    return null;

}

public static String decrypt(String strToDecrypt, String password) {
    try {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, getKey(password));
        return new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
    } catch (Exception e) {
        System.out.println("Error while decrypting: " + e.toString());
    }
    return null;
}


public static void main(String args[]) {
    String text = "Hello World!";
    String encrypt = AES.encrypt(text,SALT);

    System.out.println("String to Encrypt: " + text);
    System.out.println("Encrypted: " + encrypt);

    System.out.println("String To Decrypt : " + encrypt);
    System.out.println("Decrypted : " + AES.decrypt(encrypt,SALT));
}

}
