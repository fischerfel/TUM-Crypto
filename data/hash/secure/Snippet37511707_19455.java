import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

public class TestStackOverflowQuestion {

    public static void main(String[] args) {
        try {
            System.out.println(makeXmlFileName("Mary", "John"));
        } catch (NoSuchAlgorithmException |UnsupportedEncodingException ex) {
            // Your error handling here!
        }
    }

    private static String makeXmlFileName(String username1, String username2) 
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.reset();
        String key;
        if (username1.compareTo(username2) > 0) {
            key = DatatypeConverter.printHexBinary(md.digest(username2.getBytes("UTF-8")))
                + DatatypeConverter.printHexBinary(md.digest(username1.getBytes("UTF-8")));
        } else {
            key = DatatypeConverter.printHexBinary(md.digest(username1.getBytes("UTF-8")))
                + DatatypeConverter.printHexBinary(md.digest(username2.getBytes("UTF-8")));
        }
        return DatatypeConverter.printHexBinary(md.digest(key.getBytes("UTF-8"))) + ".xml";
    }
}
