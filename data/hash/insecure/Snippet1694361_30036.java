package com.stackoverflow.q1684963;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Sum {
    public static String calc(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(data);
        BigInteger digestValue = new BigInteger(1, digest);
        return String.format("%032x", digestValue);
    } 
}
