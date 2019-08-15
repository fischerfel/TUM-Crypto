package org.temp2.cod1;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;

public class Code1 {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
    String s = "9882623867";
    byte[] plaintext = s.getBytes("UTF-16");
    String s2 = "supernova";
    byte[] key = s2.getBytes("UTF-16");
    Cipher c = Cipher.getInstance("AES");
    SecretKeySpec k =  new SecretKeySpec(key, "AES");
    c.init(Cipher.ENCRYPT_MODE, k);
    byte[] encryptedData = c.doFinal(plaintext);
    System.out.println(encryptedData);
}
}
