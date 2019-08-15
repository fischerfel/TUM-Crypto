package de.lps.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class PasswordHash {
    /**
     * 
     * @param password
     * @return returns the md5-hash of the password if the password == null  returns null
     */
    public static String hash(String password){
        if(null == password){
            return null;
        }
        MessageDigest sha256;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
            byte[] passBytes = password.getBytes("UTF-8");
            byte[] passHash = sha256.digest(passBytes);
            return DatatypeConverter.printHexBinary(passHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }        

    }

    /**
     * Compares a specified password with a specified hash
     * @param password
     * @param hash
     * @return return true when hash and password are equals
     */
    public static boolean comparePasswordWithHash(String password, String hash){
        if(hash(password).equals(hash)){
            return true;
        }
        return false;
    }
}   
