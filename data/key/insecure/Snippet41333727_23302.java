package main;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class ForTest {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        String message= "0123456789012345";//Message to encode  
        String key = "2222222222222222";  
        // 128 bit key  -this key is processed as ASCII values  
        System.out.println("Processing 3.0 AES-128 ECB Encryption/Decryption Example");
        System.out.println("++++++++++++++++++++++++++++++++");
        System.out.println("Original Message: " + message);
        System.out.println("Key: " + key);
        System.out.println("key in bytes: "+key.getBytes("UTF-8"));
        System.out.println("==========================");           
        //Encrypter
        SecretKeySpec skeySpec_encode = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher_encode  = Cipher.getInstance("AES/ECB/NoPadding");
        //          Cipher cipher_encode = Cipher.getInstance("AES/ECB/PKCS5PADDING"); //AES-CBC with IV encoding, ECB is used without the IV, example shown on <a href="http://aesencryption.net/" target="_blank" rel="nofollow">http://aesencryption.net/</a> 
        cipher_encode.init(Cipher.ENCRYPT_MODE, skeySpec_encode);
        byte[] encrypted = cipher_encode.doFinal(message.getBytes());
        System.out.println("Encrypted String (base 64): "
                + DatatypeConverter.printBase64Binary(encrypted));
        //encode without padding: Base64.getEncoder().withoutPadding().encodeToString(encrypted));
        //encode with padding:  Base64.getEncoder().encodeToString(encrypted));
        String base64_encrypted = DatatypeConverter.printBase64Binary(encrypted);
        //Decrypter
        SecretKeySpec skeySpec_decode = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher_decode  = Cipher.getInstance("AES/ECB/NoPadding");
        //          Cipher cipher_decode = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher_decode.init(Cipher.DECRYPT_MODE, skeySpec_decode);
        System.out.println("length: "+"Ouril+UTDF8htLzE".length());
        byte[] decrypted_original = cipher_decode.doFinal(DatatypeConverter.parseBase64Binary("Ouril+UTDF8htLzEhiRj7wA="));
        String decrypt_originalString = new String(decrypted_original);
        System.out.println("Decrypted String: " + decrypt_originalString);
    }
}
