package main;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class Prueba {

    private static final String keyValue = "fd<[;.7e/OC0W!d|";
    private static final String ALG = "Blowfish";

    public static void main(String[] args) {
        String text = "some random text";

        try {
            SecretKeySpec key = new SecretKeySpec(keyValue.getBytes(), ALG);
            Cipher cipher = Cipher.getInstance(ALG);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(text.getBytes());
            String encrypted = new String(encryptedBytes);

            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] recoveredBytes = cipher.doFinal(encrypted.getBytes());
            String recovered = new String(recoveredBytes);

        } catch (NoSuchAlgorithmException nsa) {
            nsa.printStackTrace();
        } catch (NoSuchPaddingException nspe) {
            nspe.printStackTrace();
        } catch (InvalidKeyException ike) {
            ike.printStackTrace();
        } catch (BadPaddingException bpe) {
            bpe.printStackTrace();
        } catch (IllegalBlockSizeException ibse) {
            ibse.printStackTrace();
        } 
    }


}
