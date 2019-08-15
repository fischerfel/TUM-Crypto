package hashingwstest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;


public class HashingWSTest {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Password: ");
        String passwordToHash = sc.nextLine();

        byte[] bytes = getBytes();
        String salt = new String(bytes);

        String securePassword = hash256(passwordToHash, salt);
        System.out.println("Hash successfully generated");

        System.out.print("Enter your password again: ");
        String checkPassword = sc.nextLine();
        String checkHash = hash256(checkPassword,salt);
        if (checkHash.equals(securePassword)) {
            System.out.println("MATCH");
        }
        else {
            System.out.println("NO MATCH");
        }
    }

    private static String hash256(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();

            for (int i=0; i<bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private static byte[] getBytes() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] bytes = new byte[16];
        sr.nextBytes(bytes);
        return bytes;
    }
}
