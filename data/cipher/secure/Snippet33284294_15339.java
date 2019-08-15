package javaapplication1;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.binary.Hex;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import javax.xml.bind.DatatypeConverter;

public class JavaApplication1 {

    public static void main(String[] args) {
        byte[] ENCRYPTION_KEY = JavaApplication1.toByteArray("SECRET_KEY_GOES_HERE");
        String INPUT = new String("cBVlMjBttr7DKW8fhHtqJOLyMBNrgxpIJsgFFPjkA/4MWxMIudOnYzS4WuxIhUjtgGgk4CzrkJ1G60R4OWBljNTMA9ATPKh9PXe7wXAwJfE9zc698bQv4lDkXRME+q4xCb3bK/UGQ/BPVIkmRYdHcBvIHXNzGd36Nn40giigY/g=");

        try {
            System.out.println(JavaApplication1.decryptStringAES(INPUT, ENCRYPTION_KEY));
        } catch (Exception exception) {
            System.out.println("Error occured: " + exception);
        }
    }

    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    public static String decryptStringAES(String input, byte[] key) throws Exception {
        byte[] IV = JavaApplication1.toByteArray("00000000000000000000000000000000");

        byte[] inputBytes = Base64.decodeBase64(input.getBytes());
        Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new
        IvParameterSpec(IV));
        byte[] decrypt = decryptCipher.doFinal(inputBytes);
        return new String(decrypt);
    }
}
