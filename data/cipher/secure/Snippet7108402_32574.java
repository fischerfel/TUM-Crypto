package com.emap.services;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.Deflater;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
public class CompressEncrypt {
    static byte[] ibv = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a,
        0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10};

    public String compressEncryptData() {

        String strData = "Testing compression(Deflate Algorothjm), encrption(AES algorithm) for WP7.";
        byte[] inputData = strData.getBytes();
        // supporting maximum 10 MB of data
        byte[] outputData = new byte[1024 * 1024 * 10];
        // initiate compressor instance
        Deflater compresser = new Deflater();
        // compressed base64 encoded string

        byte [] compressedByte = null;

        int compressedDataLength = 0;
        //Encrypted String
        String encryptedStr = "";

        try {
            compresser.setInput(inputData, 0, inputData.length);
            compresser.finish();
            compressedDataLength = compresser.deflate(outputData);
            compressedByte = Arrays.copyOfRange(outputData, 0, compressedDataLength);
            System.out.println("Compressed String is : " + compressedByte.toString());
        } catch (Exception ex) {
            System.out.println("Error : " + ex.getMessage());
        }

        try {
            SecretKeySpec skeySpec = new SecretKeySpec("E2D5@eMap_AndIBB".getBytes(), "AES");

            IvParameterSpec iv = new IvParameterSpec(ibv);
            // Instantiate the cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(compressedByte);
            System.out.println("Encrypted String is : " + encrypted.toString());
            encryptedStr = Base64.encode(encrypted);
        }  catch (Exception ex) {
            System.out.println("No Such Padding Error: " + ex.getMessage());
            encryptedStr = "No Such Padding Error: " + ex.getMessage();
        }
        System.out.println("Compressed Encrypted Encoded String is : " + encryptedStr);
        return encryptedStr;
    }
}
