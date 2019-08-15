import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class Encryption {
    public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
                String t = "Testing";

                byte[] dataToSend = t.getBytes();
                byte[] key = new byte[16];

                Cipher c = Cipher.getInstance("AES");
                SecretKeySpec k = new SecretKeySpec(key, "AES");
                c.init(Cipher.ENCRYPT_MODE, k);
                byte[] encryptedData = c.doFinal(dataToSend);
                System.out.println(encryptedData);

                byte[] key2 = new byte[16];
                        byte[] encryptedData2 = encryptedData;

                        Cipher c2 = Cipher.getInstance("AES");
                        SecretKeySpec k2 =
                          new SecretKeySpec(key2, "AES");
                        c2.init(Cipher.DECRYPT_MODE, k2);
                        byte[] data = c.doFinal(encryptedData);
                        System.out.println(data);

    }
}
