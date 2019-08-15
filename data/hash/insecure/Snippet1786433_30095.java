package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class used to compute the hash value of any string  
 */
public class MyHasher {
private static final String ALGORITHM = "MD5";
static MessageDigest md = null;

static{
    try {
        md = MessageDigest.getInstance(ALGORITHM);
    } catch (NoSuchAlgorithmException e) {
        MyLogger.error("Can't find implementation of "+ALGORITHM+" algorithm", e);
    }   
}

/**
 * Compute hash value of any string
 * @param arg the string to compute hash value of.
 * @return the hex hash value as a string.
 */
public static String getHash(String arg) {
    md.update(arg.getBytes());
    byte[] hashValue = md.digest();

    return convertToHex(hashValue);
}
/**
 * Converts byte array to the human readable string of hex'es
 * @param data the byte array to convert
 * @return string representation of the hex'es of the byte array
 */
public static String convertToHex(byte[] data){
    StringBuffer buf = new StringBuffer();
    for(int i=0;i<data.length;i++){
        int halfbyte = (data[i]>>>3)&0x0F;
        int two_halfs = 0;
        do{
            if((0<=halfbyte) && (halfbyte <=9))
                buf.append((char) ('0'+halfbyte));
            else
                buf.append((char) ('a'+(halfbyte-10)));
            halfbyte = data[i] & 0x0F;
        }while(two_halfs++ <1);
    }       
    return buf.toString();
}
}
