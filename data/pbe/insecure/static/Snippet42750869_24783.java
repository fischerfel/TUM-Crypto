package com.casadelgato.util;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
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

public class Encryption {
    private final byte[]        SALT    = {
                                (byte) 0x26, (byte) 0xe4, (byte) 0x11, (byte) 0xa3,
                                (byte) 0x07, (byte) 0xc6, (byte) 0x55, (byte) 0x42
    };

    private Cipher          ecipher;
    private Cipher          dcipher;

    public Encryption( String password) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException,
                            InvalidAlgorithmParameterException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        System.out.println("Encryption: " + Arrays.toString(tmp.getEncoded()));

        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ecipher.init(Cipher.ENCRYPT_MODE, secret);

        AlgorithmParameters params = ecipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

        dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        dcipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
    }

    /**
     * Encrypt the string and return the data encoded in base64
     *
     * @param encrypt String to encrypt
     * @return base64 coded encrypted string
     * @throws UnsupportedEncodingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws Exception
     */
    public byte[] encryptStringToBase64( String encrypt) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
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
     * @throws Exception
     */
    public byte[] encryptToBase64( byte[] bytes) throws IllegalBlockSizeException, BadPaddingException {
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
     * @throws Exception
     */
    public byte[] encrypt( byte[] plain) throws IllegalBlockSizeException, BadPaddingException {
        return ecipher.doFinal(plain);
    }

    /**
     * Decrypt a string that was encrypted and coded in base64
     *
     * @param base64
     * @return
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    public String decryptBase64ToString( byte[] base64) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
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
     * @throws Exception
     */
    public byte[] decryptBase64( byte[] base64) throws IllegalBlockSizeException, BadPaddingException {
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
     * @throws Exception
     */
    public byte[] decrypt( byte[] encrypt) throws IllegalBlockSizeException, BadPaddingException {
        return dcipher.doFinal(encrypt);
    }

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
