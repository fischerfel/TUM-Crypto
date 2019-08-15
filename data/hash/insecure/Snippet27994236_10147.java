/* package whatever; // don't place package name! */

import java.util.*;
import java.lang.*;
import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */
class Ideone
{
    public static void main (String[] args) throws java.lang.Exception
    {
        String salt = "363";
        String password = "password";
        String md5 = MD5(MD5(salt) + MD5(password));

        System.out.println("MD5: " + md5);
    }

    public static String MD5(String md5) {
           try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
                byte[] array = md.digest(md5.getBytes());
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < array.length; ++i) {
                  sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
               }
                return sb.toString();
            } catch (java.security.NoSuchAlgorithmException e) {
            }
            return null;
    }
}
