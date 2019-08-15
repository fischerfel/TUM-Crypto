import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

public class JavaCipher {

    private SecretKeySpec secretKey;

    private JavaCipher(String secret) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] digest = sha.digest(secret.getBytes("UTF-8"));
        secretKey = new SecretKeySpec(digest, "AES");
    }

    private String encrypt(String sSrc) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
        return DatatypeConverter.printHexBinary(encrypted).toLowerCase();
    }

    public static void main(String[] args) throws Exception {  
        JavaCipher cipher = new JavaCipher("some random key");

        // print d013acccb5d191a00898ac87057383ff
        System.out.println(cipher.encrypt("abcdefg"));
    }
}
