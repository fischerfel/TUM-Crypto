package tests;

import java.security.MessageDigest;

/**
 * Created by IntelliJ IDEA.
 * User: Iraklis
 * Date: 2 Ιουν 2010
 * Time: 2:15:03 μμ
 * To change this template use File | Settings | File Templates.
 */
public class Md5Test {
    public static String encryptPassword(String password) {
        String encrypted = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] passwordBytes = password.getBytes();

            digest.reset();
            digest.update(passwordBytes);
            byte[] message = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < message.length; i++) {
                hexString.append(Integer.toHexString(
                        0xFF & message[i]));
            }
            encrypted = hexString.toString();
        }
        catch (Exception e) {
        }
        return encrypted;
    }

    public static void main(String[] args) {
        System.out.println("Pass1 md5 = " + encryptPassword("Test123FORXTREMEpass"));
        System.out.println("Pass1 md5 = " + encryptPassword("Ijdsaoijds"));
        System.out.println("Pass1 md5 = " + encryptPassword("a"));
        System.out.println("Pass1 md5 = " + encryptPassword(" "));
    }

}


Output:
Pass1 md5 = dc3a7b42a97a3598105936ef22ad2c1
Pass1 md5 = df7ca542bdbf7c4b8776cb21c45e7eef
Pass1 md5 = cc175b9c0f1b6a831c399e269772661
Pass1 md5 = 7215ee9c7d9dc229d2921a40e899ec5f
