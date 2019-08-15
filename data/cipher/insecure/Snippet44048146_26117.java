import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Gen_DESAES_key {

    public static void main(String[] args) throws NoSuchAlgorithmException,
                                                  NoSuchPaddingException,
                                                  BadPaddingException,
                                                  IllegalBlockSizeException,
                                                  InvalidKeyException {

        byte[] message = "Hello World".getBytes();

        KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
        SecretKey desKey = keygenerator.generateKey();

        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        desCipher.init(Cipher.ENCRYPT_MODE, desKey);

        byte[] encryptedMessage = desCipher.doFinal(message);
        System.out.println(new String(encryptedMessage));
    }
}
