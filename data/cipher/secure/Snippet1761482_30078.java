package org.temp2.cod1;
import java.security.*;
import java.security.spec.KeySpec;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;

public class Code2 {

public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(password, salt, 1024, 256);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = cipher.getParameters();
    byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
    byte[] ciphertext = cipher.doFinal("Hello, World!".getBytes("UTF-8"));


    }
}
