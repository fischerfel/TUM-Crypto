package com.test.so;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class MessageHashTest {

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String algorithm = "hmacSHA256";
        String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum id urn";
        String secretKey = "5771CC06-B86D-41A6-AB39-9CA2BA338E27";

        Mac mac = Mac.getInstance(algorithm);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getBytes("US-ASCII"), 
        mac.getAlgorithm());
        mac.init(secret);
        String signature = new String(Base64.encodeBase64(mac.doFinal(message.getBytes("US-ASCII"))));
        System.out.println(signature);
    }
}
