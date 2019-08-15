import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1 {
    private String value = null;

    public Sha1(final String input) {
        final StringBuffer sb = new StringBuffer();
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            final byte[] result = mDigest.digest(input.getBytes());

            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }

            value = sb.toString();
        } catch (NoSuchAlgorithmException nsae) {
            value = null;
            nsae.printStackTrace();
        }
    }

    public String getValue() {
        return value;
    }

    public static void main(final String[] args) {
        final Sha1 testSha1 = new Sha1("test");
        System.out.println(testSha1.getValue());
    }
}
