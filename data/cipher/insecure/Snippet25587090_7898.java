import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class DecryptCipherAES {

    public static void main(String[] args) {
        try {
            byte[] key = ("dfaa3b49adbc546d4437107b6a666cb1").getBytes();
            SecretKey secretKey = new SecretKeySpec(key, "AES");

            String base64String = "iwEjj0Gahfzgq4BWrdY9odNX9PqvHgppz9YZ3mddQq8=";
            byte[] enc = org.apache.commons.codec.binary.Base64.decodeBase64(base64String.getBytes());

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            String dec = new String(cipher.doFinal(enc));
            System.out.println("texte decrypte : " + dec);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }
}
