package com.example.javatest;

import java.security.MessageDigest;

public class StartingPoint {
    public static void main(String[] args) throws Exception{
        System.out.println(getSHA1("ABC"));
    }


    public static String getSHA1(String s) throws Exception{ // Generate SHA1 hash
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(s.getBytes("iso-8859-1"), 0, s.length());
        byte[] sha1Hash = md.digest();
        return convertToHex(sha1Hash);
    }


    public static String convertToHex(byte[] data) { // Convert to a HEX string
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

}
