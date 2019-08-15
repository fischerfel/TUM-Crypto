public class EncryptionTest{

public static void main(String[] args) {        
    String encrypt = new String(encrypt("1234567890123456"));
    System.out.println("decrypted value:" + (decrypt("ThisIsASecretKey",encrypt)));
}

public static String encrypt(String value) {
    try {
        byte[] raw = new byte[]{'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(value.getBytes());
        System.out.println("encrypted string:" + (new String(encrypted)));
        return new String(skeySpec.getEncoded());
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return null;
}

public static String decrypt(String key, String encrypted) {
    try {
        SecretKeySpec skeySpec = new SecretKeySpec(Base64.decodeBase64(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(skeySpec.getEncoded(),"AES"));
            (*)
        byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
        original.toString();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return null;
}  
}
