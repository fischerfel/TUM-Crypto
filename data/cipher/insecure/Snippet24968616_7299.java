import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class stackoverflow_test {
    private byte[] encrypted;

    private String encryptedtext;
    private String decrypted;

    public String Encrypt(String pInput) {

        try {

            String Input = pInput;
            String key = "Bar12345Bar12345Bar12345Bar12345";

            SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(Input.getBytes());
            //encryptedtext = new String(encrypted);
            encryptedtext = DatatypeConverter.printBase64Binary(encrypted);
            System.err.println("encrypted:" + encryptedtext);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedtext;
    }

    public String Decrypt(String pInput) {

        try {

            String Input = pInput;

            String key = "Bar12345Bar12345Bar12345Bar12345";

            SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            encrypted = DatatypeConverter.parseBase64Binary(encryptedtext);
            decrypted = new String(cipher.doFinal(encrypted)); 
            System.err.println("decrypted: " + decrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return pInput;
    }

    public static void main(String[] ag){
        stackoverflow_test test = new stackoverflow_test();
        String a = test.Encrypt("Byte cannot directly convert to string");
        String b = test.Decrypt(a);
    }
}
