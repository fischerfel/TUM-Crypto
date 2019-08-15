package RestApi.Tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.glassfish.jersey.internal.util.Base64;


/**
 *  @file tools.java
 *  @brief this file contains some hasher like MD5.
 *  for decoding base 64 we had to use an library because java.util.Base64 
 *  is for jdk 8 and our server working with jdk 7.
 *
 *  @author Seyed Ali Roshan
 */
public class tools {

    public String md5(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(text.getBytes());

        byte byteData[] = md.digest();


        StringBuilder hexString = new StringBuilder();
        for (int i=0;i<byteData.length;i++) {
            String hex=Integer.toHexString(0xff & byteData[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String[] conventToUserPass(String text) {
        String patternText = "(?i)Basic ";
        String hashedString = text.replaceFirst(patternText, "");

        byte[] unhased = Base64.decode(hashedString.getBytes());
        String finalText = new String(unhased);

        return finalText.split(":");
    } 
}
