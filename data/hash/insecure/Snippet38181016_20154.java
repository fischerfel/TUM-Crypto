package sample;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
 import java.io.File;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
public class MD5 {
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
     public static void main(String[] args) throws Exception{
         if (args.length != 3) {
             System.out.println("Usage: java XSLTransformer inputfile.xml inputfile.xsl outputfile");
             System.exit(1);
         }
         Transformer transformer =
             TransformerFactory.newInstance().newTransformer(new StreamSource(new File(args[1])));
         transformer.transform(new StreamSource(new File(args[0])),
                               new StreamResult(new File(args[2])));
         }
 }