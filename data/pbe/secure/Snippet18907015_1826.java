package se.vwl.support;

import java.security.AlgorithmParameters;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    SecretKeyFactory factory = null;
    KeySpec spec = null;
    SecretKey tmp = null;
    SecretKey secret = null;

    Cipher cipher = null;
    byte[] iv = null;
    AlgorithmParameters params = null;

    public AES(String pwd, String slz) throws Exception {
        char[] password = pwd.toCharArray();
        factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] salt = slz.getBytes();
        spec = new PBEKeySpec(password, salt, 65536, 128);
        tmp = factory.generateSecret(spec);
        secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        // //////////////////////////////////////////////////////////////////

        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        params = cipher.getParameters();
        iv = params.getParameterSpec(IvParameterSpec.class).getIV();

    }

    public static void main(String args[]) {
        AES x = null;
        try {
            x = new AES("pwd", "salt");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String test = "hello";
        byte[] tmp = null;
        try {
                tmp = x.encrypt2(test);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        System.out.println("Enc_" + test);
    }

    public byte[] encrypt2(String str) throws Exception {
        byte[] ciphertext = null;

        ciphertext = cipher.doFinal(str.getBytes("UTF-8"));


        for (int i = 0; i < ciphertext.length; i++) {
            System.out.print(ciphertext[i] + "|");
        }
        System.out.println("");
        return ciphertext;
    }

    public String decrypt2(byte[] enc) throws Exception {
        String plaintext = null;

        //cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
        plaintext = new String(cipher.doFinal(enc), "UTF-8");
        // System.out.println(plaintext);

        return plaintext;
    }
}
