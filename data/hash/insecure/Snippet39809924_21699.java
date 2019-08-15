import java.security.MessageDigest;
import java.util.Scanner;

class CredAndPass {
    public static void CredAndPass() throws Exception {
        String original = "letmein";  //Replace "password" with the actual password inputted by the user
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(original.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
    }
}
public class AuthSystem {

    public static void main(String[] args) throws Exception {
        int i;
        String username = "";
        String password = "";
        for (i = 0; i <= 2; ++i){
            Scanner scnr = new Scanner(System.in);
            System.out.println("Enter username:");
            username = scnr.next();
            System.out.println("Enter password:");
            String password1 = scnr.next();
        }
    }
}
