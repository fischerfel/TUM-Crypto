import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import static java.nio.charset.StandardCharsets.UTF_16LE;

public class Md5Sum {
    public static void main(final String... args) {
        final String md5sum = md5sum("dbox#service" + "2014-12-24T18:34:49");
        System.out.println("MD5 hash generated is: " + md5sum);
    }

    public static String md5sum(final String plaintext) {
        return toHexString(md5sum(plaintext.getBytes(UTF_16LE)));
    }

    private static byte[] md5sum(final byte... bytes) {
        final MessageDigest digest = createMD5();
        digest.update(bytes);
        return digest.digest();
    }

    private static MessageDigest createMD5() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (final NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    private static String toHexString(final byte... bytes) {
        final Formatter formatter = new Formatter();
        for (final byte b : bytes)
            formatter.format("%02X", b);
        return formatter.toString();
    }
}
