import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Owasp {

    private final static int ITERATION_NUMBER = 5;

    public void testhash() throws IOException, NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] bSalt = base64ToByte("123456");
        byte[] bDigest = getHash(ITERATION_NUMBER, "somepass", bSalt);
        String sDigest = byteToBase64(bDigest);
        System.out.println(sDigest);
    }

    public byte[] getHash(int iterationNb, String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(salt);
        byte[] input = digest.digest(password.getBytes("UTF-8"));
        for (int i = 0; i < iterationNb; i++) {
            digest.reset();
            input = digest.digest(input);
        }
        return input;
    }

    public static byte[] base64ToByte(String data) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(data);
    }

    public static String byteToBase64(byte[] data) {
        BASE64Encoder endecoder = new BASE64Encoder();
        return endecoder.encode(data);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException {
        Owasp t = new Owasp();
        t.testhash();
    }
}
