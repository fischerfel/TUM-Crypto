import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class StreamForwarder {

    public void forwardStream(InputStream is) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        DigestInputStream dis = new DigestInputStream(is, md);

        StreamConsumer streamConsumer = new StreamConsumer();
        streamConsumer.printStreamContent(dis);

        byte digest[] = md.digest();

        String digestUtilsHexString = DigestUtils.md5Hex(digest);
        String binaryHexString = new String(Hex.encodeHex(digest));

        System.out.println("MD5 while streaming encoded by DigestUtils: " + 
            digestUtilsHexString);
        System.out.println("MD5 while streaming encoded by binary.Hex: " + 
            binaryHexString);
    }
}
