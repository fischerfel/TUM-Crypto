package hash;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    String[] array = new String[16];
    array[0]="0"; array[1]="1"; array[1]="1"; array[2]="2"; array[3]="3"; array[4]="4"; array[5]="5"; array[6]="6"; array[7]="7"; array[8]="8";
    array[9]="9"; array[10]="a"; array[11]="b"; array[12]="c"; array[13]="d"; array[14]="e"; array[15]="f";
    int one, two, three, four, five, six, seven, eight; one=two=three=four=five=six=seven=eight=0;
    String text = ""; //this is gonna be the original text
    String hash = "ae5ce162888ee3ebe974976cac5ab94a3f55049f8515884883d579fb3fa378d2"; //this is the hash
    byte[] digest = null;
    MessageDigest md = MessageDigest.getInstance("SHA-256");

    for (int i=1; i<999999999; i++) {

    if (i%(16*16*16*16*16*16*16)==0) {
        two=three=four=five=six=seven=eight=0; one++;
        text=array[one]+array[two]+array[three]+array[four]+array[five]+array[six]+array[seven]+array[eight];
    } else {
        if (i%(16*16*16*16*16*16)==0) {
            three=four=five=six=seven=eight=0; two++;
            text=array[one]+array[two]+array[three]+array[four]+array[five]+array[six]+array[seven]+array[eight];
        } else {
            if (i%(16*16*16*16*16)==0) {
                four=five=six=seven=eight=0; three++;
                text=array[one]+array[two]+array[three]+array[four]+array[five]+array[six]+array[seven]+array[eight];
            } else {
                if (i%(16*16*16*16)==0) {
                    five=six=seven=eight=0; four++;
                    text=array[one]+array[two]+array[three]+array[four]+array[five]+array[six]+array[seven]+array[eight];
                } else {
                    if (i%(16*16*16)==0) {
                    six=seven=eight=0; five++;
                    text=array[one]+array[two]+array[three]+array[four]+array[five]+array[six]+array[seven]+array[eight];
                } else {
                    if (i%(16*16)==0) {
                    seven=eight=0; six++;
                    text=array[one]+array[two]+array[three]+array[four]+array[five]+array[six]+array[seven]+array[eight];
                    } else {
                        if (i%16==0) {
                            eight=0; seven++;
                            text=array[one]+array[two]+array[three]+array[four]+array[five]+array[six]+array[seven]+array[eight];
                        } else {
                            eight++;
                            text=array[one]+array[two]+array[three]+array[four]+array[five]+array[six]+array[seven]+array[eight];
                        }
                    }
                }
            }
        }
    }
}


    md.update(text.getBytes("UTF-8")); 
    digest = md.digest();


    if (String.format("%064x", new java.math.BigInteger(1, digest)).equals(hash)) {
        i=999999999;
    }
}
    System.out.println(text);
}
}
