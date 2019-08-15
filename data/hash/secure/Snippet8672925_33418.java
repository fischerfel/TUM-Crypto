/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CryptoLib;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class cryptoSha {

    public cryptoSha() {
    }

    /** method for converting simple string into SHA-256 hash */
       public String stringHash(String hash) throws NoSuchAlgorithmException{

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(hash.getBytes());

            byte byteData[] = md.digest();

            /** convert the byte to hex format */
            StringBuilder sb = new StringBuilder();
                for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }              
           return sb.toString();
       }


}
