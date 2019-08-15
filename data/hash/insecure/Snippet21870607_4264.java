import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TestMD5
{
private static final char[] CONSTS_HEX = {    '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f' };
public static String encryptionMD5(String token)
{
    try
    {
       MessageDigest msgd = MessageDigest.getInstance("MD5");
       byte[] bytes = msgd.digest(token.getBytes());
       StringBuilder strbMD5 = new StringBuilder(2 * bytes.length);
       for (int i = 0; i < bytes.length; i++)
       {
           int low = (int)(bytes[i] & 0x0f);
           int high = (int)((bytes[i] & 0xf0) >> 4);
           strbMD5.append(CONSTS_HEX[high]);
           strbMD5.append(CONSTS_HEX[low]);
       }
       return strbMD5.toString();
    }catch (NoSuchAlgorithmException e) {
             return null;
     }
}

public static void main(String args[])
{
    String msg01=new String("12345678910");
     String msg02=new String("12345678910 ");
    System.out.println("\n\nMD5 Encryption of" +msg01+": "+encryptionMD5(msg01));
    System.out.println("MD5 Encryption of "+msg02+":"+encryptionMD5(msg02));
}
