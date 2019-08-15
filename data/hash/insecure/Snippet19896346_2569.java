import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5Summer is a utility class that abstracts the complexity of computing
 * the MD5 sum of an array of Strings.
 * <p>
 * Submitted as an answer to the StackOverflow bounty question:
 * <a href="http://stackoverflow.com/questions/4785275/compute-md5-hash-of-multi-part-data-multiple-strings">
 * compute md5 hash of multi part data (multiple strings)</a>
 * <p>
 * This solution uses the 'fast' "byte[] to hex string" mechanism described here in
 * <a href="http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java">
 * Convert from byte array to hex string in java</a>.
 * <p>
 * The MD5 sum is always calculated by converting the inputStrings to bytes based on
 * the UTF-8 representation of those Strings. Different platforms using this class
 * will thus always calculate the same MD5sum for the same Java Strings.
 * <p>
 * Using a ThreadLocal for storing the MessageDigest instance significantly reduces the amount of time spent
 * obtaining a Digest instance from the java.security subsystem.
 * <p>
 * <i>Copyright - This code is released in to the public domain</i>
 */
public final class MD5Summer {

    /**
     * Calculate the MD5 sum on the input Strings.
     * <p>
     * The MD5 sum is calculated as if the input values were concatenated
     * together. The sum is returned as a String value containing the
     * hexadecimal representation of the MD5 sum.
     * <p>
     * The MD5 sum is always calculated by converting the inputStrings to bytes based on
     * the UTF-8 representation of those Strings. Different platforms using this class
     * will thus always calculate the same MD5sum for the same Java Strings.
     * 
     * @param values The string values to calculate the MD5 sum on.
     * @return the calculated MD5 sum as a String of hexadecimal.
     * @throws IllegalStateException in the highly unlikely event that the MD5 digest is not installed.
     * @throws NullPointerException if the input, or any of the input values is null.
     */
    public static final String digest(final String ...values) {
        return LOCAL_MD5.get().calculateMD5(values);
    }

    /**
     * A Thread-Local instance of the MD5Digest saves construct time significantly,
     * while avoiding the need for any synchronization.
     */
    private static final ThreadLocal<MD5Summer> LOCAL_MD5 = new ThreadLocal<MD5Summer>() {
        @Override
        protected MD5Summer initialValue() {
            return new MD5Summer();
        }   
    };

    private static final char[] HEXCHARS = "0123456789abcdef".toCharArray();
    private static final Charset UTF8 = Charset.forName("UTF-8");


    private final MessageDigest md5digest;

    /**
     * Private constructor - cannot create instances of this class from outside
     */
    private MD5Summer () {
        // private constructor making only thread-local instances possible.
        try {
            md5digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // MD5 should always be available.
            throw new IllegalStateException("Unable to get MD5 MessageDigest instance.", e);
        }
    }

    /**
     * Private implementation on the Thread-local instance.
     * @param values The string values to calculate the MD5 sum on.
     * @return the calculated MD5 sum as a String of hexadecimal bytes.
     */
    private String calculateMD5(final String ... values) {
        try {
            for (final String val : values) {
                md5digest.update(val.getBytes(UTF8));
            }
            final byte[] digest = md5digest.digest();
            final char[] chars = new char[digest.length * 2];
            int c = 0;
            for (final byte b : digest) {
                chars[c++] = HEXCHARS[(b >>> 4) & 0x0f];
                chars[c++] = HEXCHARS[(b      ) & 0x0f];
            }
            return new String(chars);
        } finally {
            md5digest.reset();
        }
    }

}
