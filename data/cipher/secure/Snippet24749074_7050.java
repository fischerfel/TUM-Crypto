import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Samp {

    private static String IV = "aaaaaaaaaaaaaaaa";
    private static final String UNICODE_FORMAT = "UTF8";

    private String padd(String plaintext) {
        while (plaintext.length() % 16 != 0) {
            plaintext += "\0";
        }
        return plaintext;
    }

    public String encryptString(String plaintext, String encryptionKey) {
        try {
            byte[] cipher = encrypt(padd(plaintext), encryptionKey);
            return new String(cipher, UNICODE_FORMAT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public String decryptString(String encString, String encryptionKey) {
        try {
            System.out.println("**** decryptString ****");
            System.out.println("enc = " + encString);
            System.out.println("key = " + encryptionKey);

            String decrypted = decrypt(encString.getBytes(UNICODE_FORMAT), encryptionKey);
            return decrypted;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static byte[] encrypt(String plainText, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(UNICODE_FORMAT), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes(UNICODE_FORMAT)));
        return cipher.doFinal(plainText.getBytes(UNICODE_FORMAT));
    }

    private static String decrypt(byte[] cipherText, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(UNICODE_FORMAT), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes(UNICODE_FORMAT)));
        return new String(cipher.doFinal(cipherText), UNICODE_FORMAT);
    }
    // implement methods here
    // using AES simple encryption

    public static void main(String[] args){
        String plaintext = "Hello World!";
        String key = "asdfqaqwsaerdqsw";

        Samp s = new Samp();
        String enc = s.encryptString(plaintext, key);
        System.out.println("encrypted string = " + enc);
        String dec = s.decryptString(enc, key);
        System.out.println("decrypted string = " + dec);
    }
}
