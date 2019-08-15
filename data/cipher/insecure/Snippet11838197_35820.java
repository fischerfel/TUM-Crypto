import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import javax.crypto.spec.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;


public class Main {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {

        String pw = "[E%Xr6pG-IDIA89_&=NI[AREofOy0#Mv[nJ7rO@T^PwgT!NVY*Hri@($p4luBM)ugVvbnAnWL@xGK*jBP3s$g#-XTH{e3@X*0StJ";
        String str = encode("Testing Testing Testing Testing Testing Testing Testing Testing Testing Testing Testing Testing Testing Testing Testing Testing Testing Testing Testing Testing ", pw);
        System.out.println(str);
        System.out.println(decode(str, pw));
    }

    public static String encode(String s, String p) throws Exception
    {
        String cleartext = padRight(s, s.length()+(16-(s.length()%16)));
        String key = DigestUtils.md5Hex(p);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(cleartext.getBytes());
        return Base64.encodeBase64String(encrypted);
    }

    public static String decode(String encrypted, String p) throws Exception
    {
        byte[] bts = Base64.decodeBase64(encrypted);


        String key = DigestUtils.md5Hex(p);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);



        byte[] decrypted = cipher.doFinal(bts);

        return new String(decrypted).replaceAll("\0", "");
    }

    public static String padRight(String s, int n) {
        while (s.length() < n)
        {
            s+="\0";
        }
        return s;
    }

}
