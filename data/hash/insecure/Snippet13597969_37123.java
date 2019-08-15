import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAHashing{

    public static void main(String[] args)throws Exception{
            String password = "ABC0010|txnpassword|0|Test Reference|1.00|20110616221931";
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes("UTF-8")); 
            System.out.println("Converting SHA digest output to Hex String : "+byteArrayToHexString(SHAsum(password.getBytes("UTF-8"))));
            System.out.println("Converting md.digest output to Hex String  : "+byteArrayToHexString(md.digest()));
    }

    public static byte[] SHAsum(byte[] convertme) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md.digest(convertme);
    }

    public static String byteArrayToHexString(byte[] b) {
         String result = "";
         for (int i=0; i < b.length; i++) {
             result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
         }
         return result;
    }
}
