package com.emap.services;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESEcrypt1 {

    static byte[] ibv = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a,
        0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10};

    public String encryptData() {
        String message = "Testing AES encryption-decryption amlgorithm for WP7.";
        String encryptedStr = "";
        try {
            SecretKeySpec skeySpec = new SecretKeySpec("Passkey".getBytes(), "AES");
            IvParameterSpec iv = new IvParameterSpec(ibv);
            // Instantiate the cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(message.getBytes());
            encryptedStr = Base64.encode(encrypted);
        } catch (BadPaddingException ex) {
            System.out.println("Error: " + ex.getMessage());
            encryptedStr = "error";
        } catch (IllegalBlockSizeException ex) {
            System.out.println("Error: " + ex.getMessage());
            encryptedStr = "error";
        } catch (InvalidAlgorithmParameterException ex) {
            System.out.println("Error: " + ex.getMessage());
            encryptedStr = "error";
        } catch (InvalidKeyException ex) {
            System.out.println("Error: " + ex.getMessage());
            encryptedStr = "error";
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error: " + ex.getMessage());
            encryptedStr = "error";
        } catch (NoSuchPaddingException ex) {
            System.out.println("Error: " + ex.getMessage());
            encryptedStr = "error";
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            encryptedStr = "error";
        }
        System.out.println("Encrypted: " + encryptedStr);
        return encryptedStr;
    }
}
