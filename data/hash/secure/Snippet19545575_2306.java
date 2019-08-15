import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
public String sha256(String message) throws NoSuchAlgorithmException {
    MessageDigest md1 = MessageDigest.getInstance("SHA-256");
    String resu = A9Utility.bytesToHex(md1.digest(message.getBytes()));

    System.out.println("here SHA256: resu ="+resu);
    return resu;
}
}
