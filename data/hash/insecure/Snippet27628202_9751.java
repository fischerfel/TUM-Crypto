import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CheckSumGenerator {
    public String calculateCheckSum(final String aInput) throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("SHA");

        final byte[] checkSumBytes = md.digest(aInput.getBytes());

        final String result = new String(checkSumBytes);

        return result;
    }
}
