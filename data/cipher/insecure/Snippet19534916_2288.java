import it.sauronsoftware.base64.Base64;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class DES {

    public static void main(String [] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
    {
        String msg="This is a secret message";
        byte [] msgBytes=msg.getBytes();        
        byte [] keyBytes  = {(byte)0xFE, (byte)0xDC, (byte)0xBA, (byte)0x98, (byte)0x76, (byte)0x54, (byte)0x32, (byte)0x10};
        SecretKeySpec myDesKey = new SecretKeySpec(keyBytes, "DES");

        //to encrypt a message
        String cipher=encryptMsg(msgBytes, myDesKey);

        //to decrypt a message
        String plain = decryptMsg(cipher.getBytes(), myDesKey);

        System.out.println("Original Message: "+ msg);
        System.out.println("Encrypted Message: "+ cipher);
        System.out.println("Decrypted Message: "+ plain);

    } //end main

    //encryption function
    public static String encryptMsg(byte [] msgBytes, SecretKey myDesKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher desCipher;
        // Create the cipher 
        desCipher = Cipher.getInstance("DES/ECB/NoPadding");
        desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
        byte[] textEncrypted = desCipher.doFinal(msgBytes);

        // converts to base64 for easier display.
        byte[] base64Cipher = Base64.encode(textEncrypted);
        return new String(base64Cipher);
    } //end encryptMsg

    public static String decryptMsg(byte [] cipherBytes, SecretKey myDesKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher desCipher; 
        desCipher = Cipher.getInstance("DES/ECB/NoPadding");
        desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
        byte[] textDecrypted=desCipher.doFinal(cipherBytes);

        // converts to base64 for easier display.
        byte[] base64Plain = Base64.encode(textDecrypted);
        return new String(base64Plain);
    } //end decryptMsg
} //end class
