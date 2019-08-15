import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class CryptoUtils {

    /***************************************************************************
    * @param password
    * 
    * @return String[2] { hashed password, salt }
    * 
    * @throws NoSuchAlgorithmException
    * @throws InvalidKeySpecException
    ***************************************************************************/
    public static String[] encrypt(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = new byte[16];
        new Random().nextBytes(salt);

        return encrypt(password, salt);
    }

    private static String[] encrypt(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 2048, 160);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = f.generateSecret(spec).getEncoded();

        String passHash = new BigInteger(1, hash).toString(16);
        String saltString = new BigInteger(1, salt).toString(16);

        System.out.println("password: " + password + ", hash: " + passHash);// DEBUG
        System.out.println("password: " + password + ", salt: " + saltString);

        return new String[] { passHash, saltString };
    }

    public static boolean checkPassword(String password, String hash, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] encrypted = encrypt(password, new BigInteger(salt, 16).toByteArray());

        return encrypted[0].equals(hash);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String pass1 = "abcdefg";
        String pass2 = pass1;
        String pass3 = pass1 + "h";

        String[] result = encrypt(pass1);
        String hash = result[0];
        String salt = result[1];

        System.out.println("passwords: " + pass1 + " and " + pass2 + " matched: " + checkPassword(pass2, hash, salt));
        System.out.println("passwords: " + pass1 + " and " + pass3 + " matched: " + checkPassword(pass3, hash, salt));
    }
}
