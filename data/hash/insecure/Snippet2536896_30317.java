import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.GregorianCalendar;

import sun.misc.BASE64Encoder;

private static String getBase64Code(String input) throws 
         UnsupportedEncodingException, NoSuchAlgorithmException {

    String base64 = "";

    byte[] txt = input.getBytes("UTF8");
    byte[] text = new byte[txt.length+3];

    text[0] = (byte)239;
    text[1] = (byte)187;
    text[2] = (byte)191;

    for(int i=0; i<txt.length; i++)
       text[i+3] = txt[i];

    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(text);
    byte digest[] = md.digest();

    BASE64Encoder encoder = new BASE64Encoder();
    base64 = encoder.encode(digest);

    return base64;
 }
