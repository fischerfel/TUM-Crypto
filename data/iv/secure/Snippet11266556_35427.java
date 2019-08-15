package decryptruby;

import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class DecryptRuby {    
    public static String decrypt(String encrypted, String pwd, byte[] salt)
            throws Exception {

        String[] parts = encrypted.split("--");
        if (parts.length != 2) return null;

        byte[] encryptedData = Base64.decodeBase64(parts[0]);
        byte[] iv = Base64.decodeBase64(parts[1]);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(pwd.toCharArray(), salt, 1024, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey aesKey = new SecretKeySpec(tmp.getEncoded(), "AES");


        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(iv));

        byte[] result = cipher.doFinal(encryptedData);
        return result.toString();
    }


    public static void main(String[] args) throws Exception {
        String encrypted = "tzFUIVllG2FcYD7xqGPmHQ==--UAPvdm3oN3Hog9ND9HrhEA==";

        System.out.println("Decrypted: " + decrypt(encrypted, "password", "some salt".getBytes()));
    }
}
