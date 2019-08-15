import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class PasswordEncoder {
    private static PasswordEncoder instance;
    private final static int ITERATION_COUNT = 5;

    private PasswordEncoder() {  }

    public static synchronized PasswordEncoder getInstance() {
        if (instance == null) {
            PasswordEncoder returnPasswordEncoder = new PasswordEncoder();
            return returnPasswordEncoder;
        }
        else
            return instance;
    }

    public synchronized String encode(String password, String saltKey)throws NoSuchAlgorithmException, IOException {
        String encodedPassword = null;
        byte[] salt = base64ToByte(saltKey);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(salt);

        byte[] btPass = digest.digest(password.getBytes("UTF-8"));
        for (int i = 0; i < ITERATION_COUNT; i++) {
            digest.reset();
            btPass = digest.digest(btPass);
        }

        encodedPassword = byteToBase64(btPass);
        return encodedPassword;
    }

    private byte[] base64ToByte(String str) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] returnbyteArray = decoder.decodeBuffer(str);
        return returnbyteArray;
    }

    private String byteToBase64(byte[] bt) {
        BASE64Encoder endecoder = new BASE64Encoder();
        String returnString = endecoder.encode(bt);
        return returnString;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        String password = "Secrete@343";
        String saltKey = "PveFT7isDjGYFTaYhc2Fzw==";
        String hash1,hash2 = null;

        // Assume from UI
        PasswordEncoder encoder1 = PasswordEncoder.getInstance();
        hash1 = encoder1.encode(password, saltKey);
        System.out.println(hash1);

        // Assume the same present in db
        PasswordEncoder encoder2 = PasswordEncoder.getInstance();
        hash2 = encoder2.encode(password, saltKey);
        System.out.println(hash2);

        if(hash1.equalsIgnoreCase(hash2))
            System.out.println("Both hash Matches..");
        else
            System.out.println("Hash matches fails..");
    }
}
