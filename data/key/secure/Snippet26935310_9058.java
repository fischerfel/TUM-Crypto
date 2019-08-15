package com.company.encrypt.tests;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class TestEncryptDecrypt {

    private static final String characterEncoding = "UTF-8";
    private static final String cipherTransformation = "AES/CBC/PKCS5Padding";
    private static final String aesEncryptionAlgorithm = "AES";

    public static void main(String[] args) throws Exception {
        String key1 = "1234567812345678";
        String text = "01234567891234565";
        System.out.println("Original Text: " + text);
        String encrypted = encrypt(text, key1);
        System.out.println("Encrypted: " + encrypted);
        String decrypted = decrypt(encrypted, key1);
        System.out.println("Decrypted: " + decrypted);

    }

    public static String decrypt(String encryptedText, String key) throws Exception {
        String plainText = null;

        int keyLength = key.length();
        System.out.println("Key length: " + String.valueOf(keyLength));
        byte[] encryptedTextBytes = Base64.decodeBase64(encryptedText.getBytes());
        byte[] keyBytes = key.getBytes();

        byte[] initialVector = Arrays.copyOfRange(encryptedTextBytes, 0, keyLength);
        byte[] trimmedCipherText = Arrays.copyOfRange(encryptedTextBytes, keyLength, encryptedTextBytes.length);

        try {
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, aesEncryptionAlgorithm);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] clearText;
            clearText = cipher.doFinal(trimmedCipherText);

            plainText = new String(clearText, characterEncoding);
        } catch(NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return plainText;

    }

    public static String encrypt(String plainText, String encryptionKey) throws Exception {

        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), aesEncryptionAlgorithm);
        Cipher cipher = Cipher.getInstance(cipherTransformation);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] plainTextBytes = plainText.getBytes("UTF-8");

        byte[] encrypted = cipher.doFinal(plainTextBytes);

        return new String(Base64.encodeBase64(encrypted));
    }

}
