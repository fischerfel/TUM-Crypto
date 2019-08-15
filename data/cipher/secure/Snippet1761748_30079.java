package org.temp2.cod1;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;

public class Code2 {

public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException, InvalidParameterSpecException {

    char[] password = args[0].toCharArray();
    byte[] salt = new byte[8];
    for (int i = 0; i < 8; ++i) {
      salt[i] = (byte) Integer.parseInt(args[1].substring(i * 2, i * 2 + 2), 16);
    }


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
