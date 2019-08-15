package tools;

import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    public static String cryptString(String toCrypt) {
        String ret = "";
        try {
            String keyStr = "key";
            byte[] key = keyStr.getBytes("ASCII");
            MessageDigest sha = MessageDigest.getInstance("MD5");
            key = sha.digest(key);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] crypt = cipher.doFinal(toCrypt.getBytes("ASCII"));
            Base64.Encoder myencoder = Base64.getEncoder();
            String crypted = myencoder.encodeToString(crypt);
            ret = new String(crypted).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
    return ret;
    }

    public static String decryptString(String crypted) {
        String ret = "";
        try {
            String keyStr = "key";
            byte[] key = keyStr.getBytes("ASCII");
            MessageDigest sha = MessageDigest.getInstance("MD5");
            key = sha.digest(key);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Base64.Decoder myDecoder = Base64.getDecoder();
            byte[] encrypt = myDecoder.decode(crypted.trim().getBytes("ASCII"));
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            ret = new String(cipher.doFinal(encrypt)).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
