import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.lang.RuntimeException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.math.BigInteger;
import javax.xml.bind.DatatypeConverter;


public final class Testing {
    private static String mSalt = "fd6c3dc0165dc420b4e9225bc9bee9684387e9621b2e2c00cfffebf1ec7c30b4";

    private static String hashPassword(String password, String salt, int userId, int iterations) throws RuntimeException {
        String uid = String.valueOf(userId);
        StringBuilder pwBuilder = new StringBuilder(salt);
        pwBuilder.append(uid);
        pwBuilder.append(password);
        String rawPass = pwBuilder.toString();

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), mSalt.getBytes(), iterations, 256);
            byte[] result = skf.generateSecret(spec).getEncoded();

            StringBuilder hashBuilder = new StringBuilder(result.length * 2);

            for (byte r : result) {
                hashBuilder.append(String.format("%02x", r));
            }

            return hashBuilder.toString();
       } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException( e );
       }
    }

    public static void main(String[] args) {
        System.out.println(hashPassword("password", mSalt, 2058, 1000000));
        System.out.println("\n");
    }
}
