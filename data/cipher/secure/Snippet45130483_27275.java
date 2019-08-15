import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class MakePublicKey {

    static byte[] signature = null;


    public static void main(String[] args) {

        try {
            FileInputStream ecPubKeyFIS = new FileInputStream("D__TCC40-1.bin");

            try {
                int certificateLength = ecPubKeyFIS.available();
                byte[] certificate = new byte[certificateLength];
                ecPubKeyFIS.read(certificate);

                MakePublicKey.signature = new byte[128];
                System.arraycopy(certificate, 0, MakePublicKey.signature, 0, 128);

                // How can I make of the signature byte[] an  PublicKey Object to call the method as the following: encrypte("Hellow World!", pk)?


            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            // }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public static byte[] encrypte(String message, PublicKey pk) {

        Cipher cipher = null;
        byte[] encrypted = null;
        try {
            cipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, pk);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            encrypted = cipher.doFinal(message.getBytes());
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return encrypted;

    }

}
