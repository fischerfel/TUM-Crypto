import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;


public class MD5OnTheFly {

    /**
     * @param args
     * @throws NoSuchAlgorithmException 
     * @throws IOException 
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

        long ini = System.currentTimeMillis();

        File file = new File("/home/leoks/Downloads/VirtualBox-4.3.0.tar");

        System.out.println("size:"+file.length());

        InputStream is = new FileInputStream(file);

        MessageDigest md = MessageDigest.getInstance("MD5");

        DigestInputStream dis = new DigestInputStream(is, md);

        IOUtils.copy(dis, new NullOutputStream());

        byte[] digest = md.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(0xff & digest[i]);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }

        System.out.println(hexString);

        long end = System.currentTimeMillis();
        System.out.println(end-ini+" millis");


    }
}
