package com.my.package;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

// TODO Incomplete class
public class Encryption {

    private static final byte[] salt = { (byte) 0xA4, (byte) 0x0B, (byte) 0xC8,
            (byte) 0x34, (byte) 0xD6, (byte) 0x95, (byte) 0xF3, (byte) 0x13 };

    private static int BLOCKS = 128;

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    private static byte[] getKey() throws Exception {
        byte[] keyStart = "this is a key".getBytes();
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(keyStart);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] key = skey.getEncoded();
        return key;
    }

    public static void test() {
        String test = "My Name Is Dragon Warrior";

        byte[] e = null;
        try {
            e = encrypt(getKey(), test.getBytes());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        byte[] d = null;
        try {
            d = decrypt(getKey(), e);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        System.out.println(new String(d));
    }
}
