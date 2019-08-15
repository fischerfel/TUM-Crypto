import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;


public class DesRun {
    /**
     * @param args
     */
    public static void main(String[] args) {
        String theKey = "01234567";
        String message = "abcdefghijklmnop";
        Cipher ecipher, dcipher;
         try {
            // generate secret key using DES algorithm
             SecretKeySpec key = new SecretKeySpec(theKey.getBytes("UTF-8"), "DES");

                 ecipher = Cipher.getInstance("DES");
                 dcipher = Cipher.getInstance("DES");

                 // initialize the ciphers with the given key
                 ecipher.init(Cipher.ENCRYPT_MODE, key);
                 dcipher.init(Cipher.DECRYPT_MODE, key);

                 byte[] encrypted = ecipher.doFinal(message.getBytes("UTF-8"));
                 System.out.println(DatatypeConverter.printHexBinary(encrypted));
                 String decrypted = new String(dcipher.doFinal(encrypted), "UTF-8");

                 System.out.println("Decrypted: " + decrypted);

             }
             catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

    }
}
