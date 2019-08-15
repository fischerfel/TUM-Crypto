import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class crypto {
    public static void main(String [] args) {
        String s = args[0];
        String s1 = args[1];
        String ivkey = "1234567891234567891235478912345";
        byte[] ivraw = ivkey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(ivraw, "AES");

        if (s.equalsIgnoreCase("ENCRYPT")) {
            try {
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
                byte[] encrypted = cipher.doFinal(s1.getBytes());
                System.out.println(new String(Base64.encodeBase64(encrypted)));
                System.out.println(s1);

            } catch (NoSuchAlgorithmException | NoSuchPaddingException
                    | InvalidKeyException | IllegalBlockSizeException
                    | BadPaddingException e) {
            }
        } else {
            try {
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
                byte[] encrypted = cipher.doFinal(s1.getBytes());
                System.out.println(new String(Base64.encodeBase64(encrypted)));

            } catch (NoSuchAlgorithmException | NoSuchPaddingException
                    | InvalidKeyException | IllegalBlockSizeException
                    | BadPaddingException e) {
            }

        }
        return;
    };
}
