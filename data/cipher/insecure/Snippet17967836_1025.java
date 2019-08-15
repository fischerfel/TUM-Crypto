package com.cellapp.voda.vault.others;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;


public class Codec {

private Cipher cipher = null;
private boolean operative = true;

public Codec() {

    try {
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    } catch (NoSuchAlgorithmException e) {
        // This should not happen since we know the target platform
        // but we set the operative flag to false just in case
        operative = false;
    } catch (NoSuchPaddingException e) {
        // This should not happen since we know the target platform
        // but we set the operative flag to false just in case
        operative = false;
    }
}


   public void encrypt(byte[] keyBits, byte[] plaintext)
        throws InvalidKeySpecException, InvalidKeyException,
        IllegalStateException, ShortBufferException,
        IllegalBlockSizeException, BadPaddingException,
        InvalidAlgorithmParameterException {
        byte[] cipherText = null;
    if (operative) {
       try{
            // Initialize the key from  the password
        Key key = new SecretKeySpec(keyBits, 0, keyBits.length, "AES");
        // add 2 bytes to encode the length of the plaintext
        // as a short value
        byte[] plaintextAndLength = new byte[plaintext.length + 2];
        plaintextAndLength[0] = (byte) (0xff & (plaintext.length >> 8));
        plaintextAndLength[1] = (byte) (0xff & plaintext.length);
        // build the new plaintext
        System.arraycopy(plaintext,
                0,
                plaintextAndLength,
                2,
                plaintext.length);

        // calculate the size of the ciperthext considering
        // the padding
        int blocksize = 16;
        int ciphertextLength = 0;
        int remainder = plaintextAndLength.length % blocksize;
        if (remainder == 0) {
            ciphertextLength = plaintextAndLength.length;
        } else {
            ciphertextLength = plaintextAndLength.length - remainder
                    + blocksize;
        }
        cipherText = new byte[ciphertextLength];

        // reinitialize the cipher in encryption mode with the given key
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // do the encryption
        cipher.doFinal(plaintextAndLength,
                0,
                plaintextAndLength.length,
                cipherText,
                0);
       }
       catch(Exception e)
       {
           System.out.println("TT " + cipherText);
       }


    } else {
        throw new IllegalStateException("Codec not initialized");
    }
}

  public void decrypt(byte[] keyBits, byte[] cipherText)
        throws InvalidKeySpecException, InvalidKeyException,
        IllegalStateException, ShortBufferException,
        IllegalBlockSizeException, BadPaddingException,
        InvalidAlgorithmParameterException {
    if (operative) {
        // create a key from the keyBits
        Key key = new SecretKeySpec(keyBits, 0, keyBits.length, "AES");

        // Initialize the cipher in decrypt mode
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decrypted = new byte[cipherText.length];
        // Decrypt the cipher text
        cipher.doFinal(cipherText, 0, cipherText.length, decrypted, 0);
        // Calculate the length of the plaintext
        int plainTextLength = (decrypted[0] << 8)
                | (decrypted[1] & 0xff);
        byte[] finalText = new byte[plainTextLength];
        // Decode the final text
        System.arraycopy(decrypted, 2, finalText, 0, plainTextLength);

        System.out.println("fina;  text " + finalText);
    } else {
        throw new IllegalStateException("Codec not initialized");
    }
}

// Displays ecrypted data in hex
public String byteToHex(byte[] data) {
    StringBuffer hexString = new StringBuffer();
    String hexCodes = "0123456789ABCDEF";

    for (int i = 0; i < data.length; i++) {
        hexString.append(hexCodes.charAt((data[i] >> 4) & 0x0f));
        hexString.append(hexCodes.charAt(data[i] & 0x0f));
        if (i < data.length - 1) {
            hexString.append(":");
        }
        if (((i + 1) % 8) == 0) {
            hexString.append("\n");
        }
    }
    return hexString.toString();
}
}
