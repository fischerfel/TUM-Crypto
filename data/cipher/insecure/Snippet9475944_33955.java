import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;


public class Cryption {
    public static void cryption(String[] args, String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        byte[] encodedKey = "ADBSJHJS12547896".getBytes();
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        Key aesKey = keyGen.generateKey();

        System.out.println("CheckType: "+ Global.checkType);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] input = Global.message.getBytes();

        // Check if clicked Encrypted
        if(Global.checkType==true) {
            // Encrypt
            byte[] messageEncrypted = cipher.doFinal(input);
            System.out.println("Encrypted Text: " + messageEncrypted);
            Global.encValue = messageEncrypted.toString();
        }

        // Check if clicked Decrypted
        if(Global.checkType==false) {
            //String mes = message;
            System.out.println(Global.message);
            System.out.println("Char lenght " + Global.message.length());
            byte[] mesByte = Global.message.getBytes();


            // Decrypt
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] messageDecrypted = cipher.doFinal(mesByte);
            System.out.println("Text Decrypted: " + new String(messageDecrypted));
        }
    }

}
