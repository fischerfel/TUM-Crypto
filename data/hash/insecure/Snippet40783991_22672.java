import java.util.List;

import java.security.MessageDigest;
/*
BASIC FORMAT OF WHAT I WOULD LIKE SOMEONE TO FIX
ONLY WANT TO ENCRYPT VIA THIS METHOD*/


public class encrypt {
   /* WANT TO GET USER-INPUT AND RUN IT THROUGH THIS METHOD OF HASHING*/
    String code = array[0];
    String encrypted = encrypt(code);
/*ERRORS SO*/
    public static void main(String[] args) {
       /* WANT TO PRINT OUT RESULT HERE
        AIM IS TO GET A HASH THAT LOOKS LIKE:
        "2473511743116990591436219521516221715316",
          "101208251115150352340242201171221515066157"
        FROM THE WORD "pizzaroma"*/
        System.out.println(encrypted);
    }

    /*NO CLUE WHAT THIS DOES ^^^^ Array?*/
    private String getString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            sb.append(0xFF & b);
        }
        return sb.toString();
    }

    public String encrypt(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(source.getBytes());
            return getString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
    /*FAR:encrypt.java:6:error:cannot find symbol

        String code=array[0];

        symbol:variable array

        location:*/

/*
class encrypt

encrypt.java:35:error:non-static variable encrypted cannot be
        referenced from a static context

        System.out.println(encrypted);^

        2 errors*/
