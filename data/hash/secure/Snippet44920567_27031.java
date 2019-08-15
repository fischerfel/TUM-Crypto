import java.security.MessageDigest;
import java.security.SecureRandom;
import org.springframework.security.crypto.codec.Hex;

public class someTester{

    public static void main(String[] args) {
        String signatureInput = "someStringWhichisSensitive";
        String firstSignValue = getSignature(signatureInput);
        String secondSignValue = getSignature(signatureInput);
        System.out.println("firstSignValue="+firstSignValue);
        System.out.println("secondSignValue="+secondSignValue);
    }

    private static String getSignature(String signatureInput){
        MessageDigest md;
        String signatureValue = null;
        try {
            // Create a random salt
            SecureRandom sr = new SecureRandom();
            byte[] bSalt = new byte[8];
            sr.nextBytes(bSalt);
            md = MessageDigest.getInstance("SHA-256");
//          digest.update(bSalt);

            byte[] bDigest = md.digest(signatureInput.getBytes());
            // Iterate through 10000 times
            for (int i = 0; i < 10000; i++) {
                bDigest = md.digest(bDigest);
            }
            signatureValue = new String(Hex.encode(bDigest));
        } catch (Exception e) {
            System.out.println("Exception while calculating SHA-256 digest value"+e);
        }
        return signatureValue;
    }
}
