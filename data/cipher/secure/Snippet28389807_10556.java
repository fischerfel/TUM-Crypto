package com.test;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidParameterSpecException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.codec.binary.Base64;

public class Settings {
    private Preferences prefs = null;
    private byte[] iv = null;
    private SecretKey secret = null;
    Cipher cipher = null;

    public static void main(String[] args){
       Settings t = new Settings();
       String encText = t.encryptText("HELLO");//Encrypt a value
       String output = t.decryptText(encText);//Decrypt the value
       System.out.println(output); //Display the decrypted value.
    }

    public Settings(){
        try {
            String parentClass = new Exception().getStackTrace()[1].getClassName();//Really only controls where the prefs go, shouldn't matter.
            this.prefs = Preferences.userNodeForPackage(Class.forName(parentClass));
            Random r = new SecureRandom();
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128); // 128 bit key
            this.secret = keyGen.generateKey();

            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
        private String encryptText(String plainText){
        try {
            cipher.init(Cipher.ENCRYPT_MODE, this.secret);
            AlgorithmParameters params = cipher.getParameters();

            this.iv = prefs.getByteArray("IV", null);
            if(this.iv == null){
                this.iv = params.getParameterSpec(IvParameterSpec.class).getIV();
                prefs.putByteArray("IV", this.iv);
            }
            byte[] ciphertext = cipher.doFinal(plainText.getBytes("UTF-8"));
            String ret = new String(Base64.encodeBase64(ciphertext));
            return ret;
        } catch (InvalidParameterSpecException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidKeyException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return "";
   }
    private String decryptText(String cipherText){
        try {
            this.iv = prefs.getByteArray("IV", null);
            byte[] cText = Base64.decodeBase64(cipherText); 
            cipher.init(Cipher.DECRYPT_MODE, this.secret, new IvParameterSpec(this.iv));
            String ret = new String(cipher.doFinal(cText), "UTF-8");
            return ret;
        } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return "";
   }
}
