package com.company;
import com.hazelcast.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.*;

class Encryption {

    public static String encrypt (String strKey, String strIv, String str) {
        String secret = "";
        try{
            byte[] key = Base64.decode(strKey.getBytes());
            byte[] iv  = Base64.decode(strIv.getBytes());

            SecretKeySpec keyspec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            secret = new String(Base64.encode(cipher.doFinal(str.getBytes())));

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return secret;
    }

    public static String decrypt (String strKey, String strIv, String str) {
        String secret = "";
        try{

            byte[] key = Base64.decode(strKey.getBytes());
            byte[] iv  = Base64.decode(strIv.getBytes());


            SecretKeySpec keyspec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, keyspec,ivspec);
            secret = new String(cipher.doFinal(new Base64().decode(str.getBytes())));

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return secret;
    }

    public static void main(String[] argv) {
        String strIv = "18A5Z/IsHs6g8/65sBxkCQ==";
        String strKey = "";
        int keyStrength = 256;
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(keyStrength);

            SecretKey skey = kgen.generateKey();
            byte[] raw = skey.getEncoded();
            strKey = new String(new Base64().encode(raw));
            System.out.println("Secret key is: " + strKey);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        String message = "My, it's a lovely day today!!!";
        String encrypted = Encryption.encrypt(strKey, strIv, message);
        System.out.println("Encrypted string is: " + encrypted);
        System.out.println("Decrypted string is: " + Encryption.decrypt(strKey, strIv, encrypted));


    }
}
