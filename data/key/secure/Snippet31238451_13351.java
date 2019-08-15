package com.optixal.tests;

import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AES128ECB {

    public static void main(String[] args) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        // Input
        String hexInput = "68656c6c6f776f726c6473696e676170";
        byte[] byteInput = toByteArray(hexInput);

        // Key
        String hexKey = decToHex(77);
        byte[] byteKey = toByteArray(hexKey);

        System.out.println(" Input: " + new String(byteInput));
        System.out.println("Length: " + byteInput.length + "\n");
        System.out.println("   Key: " + hexKey);
        System.out.println("Length: " + byteKey.length + "\n");

        SecretKeySpec key = new SecretKeySpec(byteKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

        // Encrypt
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = new byte[cipher.getOutputSize(byteInput.length)];
        int ctLength = cipher.update(byteInput, 0, byteInput.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
        System.out.println("Cipher: " + toHexString(cipherText));
        System.out.println("Length: " + ctLength + "\n");

        // Decrypt
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
        int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
        ptLength += cipher.doFinal(plainText, ptLength);
        System.out.println("Output: " + new String(plainText));
        System.out.println("Length: " + ptLength);
    }

    private static final String hexDigits = "0123456789ABCDEF";
    private static final char[] hexDigitsArray = hexDigits.toCharArray();

    public static String decToHex(int dec) {

        final int sizeOfIntInHalfBytes = 32;
        final int numberOfBitsInAHalfByte = 4;
        final int halfByte = 0x0F;

        StringBuilder hexBuilder = new StringBuilder(sizeOfIntInHalfBytes);
        hexBuilder.setLength(sizeOfIntInHalfBytes);
        for (int i = sizeOfIntInHalfBytes - 1; i >= 0; --i) {
            int j = dec & halfByte;
            hexBuilder.setCharAt(i, hexDigitsArray[j]);
            dec >>= numberOfBitsInAHalfByte;
        }
        return hexBuilder.toString();
    }

    public static String toHexString(byte[] array) {
        return DatatypeConverter.printHexBinary(array);
    }

    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

}
