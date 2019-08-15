package com.encrytiondecryption;

import android.util.Base64;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class ApiCrypter5 {
    byte[] sessionKey = {your 16 character key}; //Where you get this from is beyond the scope of this post
    private byte[] iv = {your 16 character value }; //Ditto
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;

    public ApiCrypter5() {
        ivspec = new IvParameterSpec(iv);
        keyspec = new SecretKeySpec(sessionKey, "AES");

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public String encrytData(String text) throws Exception {

        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

        byte[] results = cipher.doFinal(text.getBytes());

        return Base64.encodeToString(results, Base64.NO_WRAP | Base64.DEFAULT);

    }


    public String decryptData(String text) throws Exception {
        byte[] encryted_bytes = Base64.decode(text, Base64.DEFAULT);

        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

        byte[] decrypted = cipher.doFinal(encryted_bytes);

        return new String(decrypted);

    }

}
