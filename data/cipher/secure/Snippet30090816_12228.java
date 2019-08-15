package com.game;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
public class MainTest5 {
    public static byte[] encrypt(String content, String key) {
        try {
            Cipher aesECB = Cipher.getInstance("AES/CFB/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(key.getBytes());
            aesECB.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] result = aesECB.doFinal(content.getBytes());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(new String(encrypt("1234567890123456", "1234567890123456"), "ISO_8859_1"));
        System.out.println(new String(encrypt("1234567890123457", "1234567890123456"), "ISO_8859_1"));
    }
}
