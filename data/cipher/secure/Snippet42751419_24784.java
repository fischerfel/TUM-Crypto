package com.casadelgato.util;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * General data encryption/decryption handling. Can do Strings or byte[].
 *
 * @author John Lussmyer
 */
public class Encryption {
    private String          password;
    private SecretKeyFactory    factory;

    public Encryption( String password) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException,
                            InvalidAlgorithmParameterException {
        this.password = password;
        factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        return;
    }

    /**
     * Encrypt the string and return the data encoded in base64
     *
     * @param encrypt String to encrypt
     * @return base64 coded encrypted string
     * @throws UnsupportedEncodingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidParameterSpecException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws Exception
     */
    public byte[] encryptStringToBase64( String encrypt)    throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException,
                                        NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException {
        byte[] bytes = encrypt.getBytes("UTF8");
        return encryptToBase64(bytes);
    }

    /**
     * Encrypt a block of data and encode to Base64
     *
     * @param bytes
     * @return base64 encoded encrypted data
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidParameterSpecException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws Exception
     */
    public byte[] encryptToBase64( byte[] bytes)    throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException,
                                    NoSuchPaddingException, InvalidParameterSpecException {
        byte[] encrypted = encrypt(bytes);
        return Base64.encodeBase64(encrypted);
    }

    /**
     * Encrypt a block of data
     *
     * @param plain
     * @return encryped data
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidParameterSpecException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws Exception
     */
    public byte[] encrypt( byte[] plain)    throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException,
                                InvalidParameterSpecException {
        byte[] salt = new byte[8];
        byte[] iv;
        SecureRandom rand = new SecureRandom();
        rand.nextBytes(salt);

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);

        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);

        AlgorithmParameters params = cipher.getParameters();
        iv = params.getParameterSpec(IvParameterSpec.class).getIV();

        byte[] data = cipher.doFinal(plain);
        byte[] result = new byte[data.length + salt.length + iv.length];
        System.arraycopy(salt, 0, result, 0, salt.length);
        System.arraycopy(iv, 0, result, salt.length, iv.length);
        System.arraycopy(data, 0, result, salt.length + iv.length, data.length);

        return result;
    }

    /**
     * Decrypt a string that was encrypted and coded in base64
     *
     * @param base64
     * @return
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws Exception
     */
    public String decryptBase64ToString( byte[] base64) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeyException, InvalidAlgorithmParameterException,
                                        NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
        byte[] decrypted = decryptBase64(base64);
        return new String(decrypted, "UTF8");
    }

    /**
     * Decrypt a Base64 encoded block
     *
     * @param base64
     * @return
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws Exception
     */
    public byte[] decryptBase64( byte[] base64) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
                                    NoSuchPaddingException, InvalidKeySpecException {
        byte[] decodedData = Base64.decodeBase64(base64);
        byte[] decrypted = decrypt(decodedData);
        return decrypted;
    }

    /**
     * Decrypt a binary array.
     *
     * @param encrypt
     * @return
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws Exception
     */
    public byte[] decrypt( byte[] encrypt)  throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
                                NoSuchPaddingException, InvalidKeySpecException {
        byte[] desalt = new byte[8];
        byte[] deiv = new byte[16];
        byte[] data = new byte[encrypt.length - 8 - 16];
        System.arraycopy(encrypt, 0, desalt, 0, desalt.length);
        System.arraycopy(encrypt, desalt.length, deiv, 0, deiv.length);
        System.arraycopy(encrypt, deiv.length + desalt.length, data, 0, encrypt.length - deiv.length - desalt.length);

        KeySpec spec = new PBEKeySpec(password.toCharArray(), desalt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);

        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(deiv));

        return cipher.doFinal(data);
    }

    /**
     * Used to test the code.
     *
     * @param args ignored
     * @throws Exception
     */
    public static void main( String[] args) throws Exception {
        String messages[] = { "GETP", "Testing stuff that is longer" };
        String password = "SanityLost";

        try {
            Encryption app = new Encryption(password);
            Encryption app1 = new Encryption(password);

            for (String message : messages) {
                byte[] encrypted = app.encryptStringToBase64(message);
                System.out.println("Encrypted string is: " + new String(encrypted, "UTF-8") + ", " + encrypted.length);

                String decrypted = app.decryptBase64ToString(encrypted);
                System.out.println("Decrypted string is: " + decrypted);

                decrypted = app1.decryptBase64ToString(encrypted);
                System.out.println("App1 Decrypted string is: " + decrypted);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return;
    }
}
