package abc.xyz;
import java.io.UnsupportedEncodingException; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 

public class SHA2{ 

    private static String convertToHex(byte[] data) { 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    }      
public static String SHA2(String text) 
            throws NoSuchAlgorithmException, UnsupportedEncodingException  { 

        MessageDigest mesd;
        mesd = MessageDigest.getInstance("SHA-2");
        byte[] sha2hash = new byte[40];
        mesd.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha2hash = mesd.digest();//error
        return convertToHex(sha2hash);
    } }
