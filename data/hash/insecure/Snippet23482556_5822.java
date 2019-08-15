import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Test003 {

    public static void main(String[] args) throws Exception {
        System.out.println(getPseudoUniqueString("test1@test.com"));
        System.out.println(getPseudoUniqueString("test2@test.com"));            
    }

    private static String getPseudoUniqueString(String str) 
        throws NoSuchAlgorithmException 
    {
        MessageDigest md1 = MessageDigest.getInstance("MD5");
        md1.update(str.getBytes());
        byte[] bd1 = md1.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<bd1.length;i++) {
            String hex=Integer.toHexString(0xff & bd1[i]);
            if(hex.length()==1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString().substring(0,11);
    }
}
