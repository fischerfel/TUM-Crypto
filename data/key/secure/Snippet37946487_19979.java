package com.spencergang.fem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Cryptographer {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    //Method for cryptography
    public static void crypto(int cipherMode, String key, File inputFile, File outputFile) throws CryptoException {
        try{
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();
        }catch(NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException ex){
            throw new CryptoException("ERROR ENCRYPTING / DECRYPTING FILE", ex);
        }
    }

    //Encryption
    public static void encrypt(String key, File inputFile, File outputFile) throws CryptoException {
        crypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }

    //Decryption
    public static void decrypt(String key, File inputFile, File outputFile) throws CryptoException {
        crypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }
}
