import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DigestDemo {

    public static byte[] getSha1(String file) {
        FileInputStream fis = null;
        MessageDigest md = null;

        try {
            fis = new FileInputStream(file);
        } catch(FileNotFoundException exc) {
            System.out.println(exc);
        }

        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException exc) {
            System.out.println(exc);
        }

        byte b = 0;
        do {

            try {
                b = (byte) fis.read();
            } catch (IOException e) {
                System.out.println(e);
            }

            if (b != -1)
                md.update(b);

        } while(b != -1);

        return md.digest();

    }

    public static void writeBytes(byte[] a) {
        for (byte b : a) {
            System.out.printf("%x", b);
        }
    }

    public static void main(String[] args) {

        String file = "C:\\Users\\Mike\\Desktop\\test.txt";
        byte[] digest = getSha1(file);
        writeBytes(digest);

    }

}
