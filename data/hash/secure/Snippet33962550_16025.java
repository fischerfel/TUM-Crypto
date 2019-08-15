import static org.junit.Assert.assertEquals;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class Sha256Test {

    @Test
    public void sha256Test() throws NoSuchAlgorithmException {
        String out = hash256("1130_11825_253402300799_1_1bcb4a27d42524de11325ec627b63878770a8651c0a0d8ddfc8fc06b92aea281634ff11f7d874c03851932304601439e");
        String in  = "01a9d698f0587a25ad8ef56b0994ec0022364aff91d668a4b3a4b97c40167672";
        assertEquals(in, out);
    }

    private String hash256(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data.getBytes());
        return bytesToHex(md.digest());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) {
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }
}
