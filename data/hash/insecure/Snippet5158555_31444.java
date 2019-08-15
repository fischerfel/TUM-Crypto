import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Sha1{

    private static final char[] HEX_CHARS = null;

    public static void main(String[] args){
        String hash = toSHA1(("27"+"peojvootv").getBytes());
        System.out.println(hash);
    }
    public static String toSHA1(byte[] convertme) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] buf = md.digest(convertme);
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i) {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }
}
