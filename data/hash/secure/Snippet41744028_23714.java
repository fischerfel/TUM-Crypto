import java.security.MessageDigest;
import org.junit.Test;

public class Sha512Mcve {

    private final String ENCODING = "ISO-8859-1";

    @Test
    public void test() {
        System.out.println(computeHashFor("whatever"));
    }

    private String computeHashFor(String toHash) {
        String salt = "salt";
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
//          md.update(salt.getBytes(ENCODING));
            byte[] bytes = md.digest(toHash.getBytes(ENCODING));

            return toUnixRepresentation(salt, bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String toUnixRepresentation(String salt, byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        sb.append("$6$");
        sb.append(salt);
        sb.append("$");

        for (int i = 0; i < bytes.length; i++) {
            int c = bytes[i] & 0xFF;
            if (c < 16) sb.append("0");
            sb.append(Integer.toHexString(c));
        }
        return sb.toString();
    }
}
