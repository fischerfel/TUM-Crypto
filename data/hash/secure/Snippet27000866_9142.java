import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created for https://stackoverflow.com/q/26928529/1266906.
 */
public class Hashs {

    public static class JavaHasher {
        private int hashCode;

        public JavaHasher() {
            hashCode = 0;
        }

        public void add(String value) {
            hashCode = 31 * hashCode + value.hashCode();
        }

        public int create() {
            return hashCode;
        }
    }

    public static class ShaHasher {
        public static final Charset UTF_8 = Charset.forName("UTF-8");
        private final MessageDigest messageDigest;

        public ShaHasher() throws NoSuchAlgorithmException {
            messageDigest = MessageDigest.getInstance("SHA-256");
        }

        public void add(String value) {
            messageDigest.update(value.getBytes(UTF_8));
        }

        public byte[] create() {
            return messageDigest.digest();
        }
    }

    public static void main(String[] args) {
        javaHash();

        try {
            shaHash();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  // TODO: implement catch
        }
    }

    private static void javaHash() {
        JavaHasher h = new JavaHasher();
        h.add("somestring");
        h.add("another part");
        h.add("eveno more");
        int hash = h.create();
        System.out.println(hash);
    }

    private static void shaHash() throws NoSuchAlgorithmException {
        ShaHasher h = new ShaHasher();
        h.add("somestring");
        h.add("another part");
        h.add("eveno more");
        byte[] hash = h.create();
        System.out.println(Arrays.toString(hash));
        System.out.println(new BigInteger(1, hash));
    }
}
