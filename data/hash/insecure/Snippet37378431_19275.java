//Made with help from http://www.mkyong.com/java/java-md5-hashing-example/

import java.security.MessageDigest;

public class md5_shellcode {

public static void main(String[] args){

    //Add your code here

}

/**
 * Md5 hash function, do not modify
 * @param dwarf Dwarf in the company of Thorin
 * @param number Number 1 - 9999
 * @param color Color of the rainbow
 * @return md5 hash of dwarf + number + color
 */
public static String md5Hash(String dwarf, int number, String color){

    try{

        MessageDigest m = MessageDigest.getInstance("MD5");

        m.update((dwarf + number + color).getBytes());

        byte b[] = m.digest();

        StringBuffer s = new StringBuffer();

        for(int i = 0; i < b.length; i++)

            s.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));

        return s.toString();

    }

    catch(Exception e){

        System.out.println("lol");

    }

    return "Error";

}

}
