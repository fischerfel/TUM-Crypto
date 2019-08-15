import java.io.*;
import java.math.BigInteger;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MsgDgt {
    public static void main(String[] args) throws IOException, DigestException, NoSuchAlgorithmException {

        FileInputStream inputstream = null;
        byte[] mybyte = new byte[1024];

        inputstream = new FileInputStream("e://notice.txt");
        int total = 0;
        int nRead = 0;
        MessageDigest md = MessageDigest.getInstance("MD5");
        while ((nRead = inputstream.read(mybyte)) != -1) {
            System.out.println(new String(mybyte));
            total += nRead;
            md.update(mybyte, 0, nRead);
        }

        System.out.println("Read " + total + " bytes");
        md.digest();
        System.out.println(new BigInteger(1, md.digest()).toString(16));
    }
}
