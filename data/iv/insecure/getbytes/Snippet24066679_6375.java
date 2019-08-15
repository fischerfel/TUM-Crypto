package aes;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EncryptionExample {

private static SecretKeySpec    key;
private static IvParameterSpec  ivSpec;
private static Cipher           cipher; 
private static byte[]           keyBytes;
private static byte[]           ivBytes;
private static int              enc_len;

public static void generateKey() throws Exception
        {

            String complex = new String ("9#82jdkeo!2DcASg");
            keyBytes = complex.getBytes();
            key = new SecretKeySpec(keyBytes, "AES");

            complex = new String("@o9kjbhylK8(kJh7"); //just some randoms, for now
            ivBytes = complex.getBytes();
            ivSpec = new IvParameterSpec(ivBytes);

            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        }

        public static String encrypt(String packet) throws Exception
        {
            byte[] packet2 = packet.getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] encrypted = new byte[cipher.getOutputSize(packet2.length)];
            enc_len = cipher.update(packet2, 0, packet2.length, encrypted, 0);
            enc_len += cipher.doFinal(encrypted, enc_len);

            return packet = new String(encrypted);
        }

        public static String decrypt(String packet) throws Exception
        {
            byte[] packet2 = packet.getBytes();
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            byte[] decrypted = new byte[cipher.getOutputSize(enc_len)];
            int dec_len = cipher.update(packet2, 0, enc_len, decrypted, 0);
HERE EXCEPTION>>>>> dec_len += cipher.doFinal(decrypted, dec_len);  <<<<<<<<<

            return packet = new String(decrypted);
        }


        // and display the results
    public static void main (String[] args) throws Exception 
    {

          // get the text to encrypt
        generateKey();
        String inputText = JOptionPane.showInputDialog("Input your message: ");

        String encrypted = encrypt(inputText);
        String decrypted = decrypt(encrypted);            

        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(),
                    "Encrypted:  " + new String(encrypted) + "\n"
                      +  "Decrypted: : " + new String(decrypted));
          .exit(0);
    }
}
