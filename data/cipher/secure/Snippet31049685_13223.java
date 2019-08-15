import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionTest {

    public static void main(String[] args) throws Exception {
        SecretKeySpec key = null;
        IvParameterSpec ivSpec = null;
        byte[] keyBytes = "usethiskeyusethiusethiskeyusethi".getBytes();
        byte[] ivBytes = "usethisIusethisI".getBytes();
        key = new SecretKeySpec(keyBytes, "AES"); //No I18N
        ivSpec = new IvParameterSpec(ivBytes);

        Cipher AesCipher = Cipher.getInstance("AES/CTR/NoPadding");


        byte[] byteText = "Your Plain Text Here".getBytes();

        AesCipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] byteCipherText = AesCipher.doFinal(byteText);
        System.out.println("Encrypted : " + new String(byteCipherText));

        AesCipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] bytePlainText = AesCipher.doFinal(byteCipherText);
        System.out.println("Double Encrypted : " + new String(bytePlainText));
    }
}
