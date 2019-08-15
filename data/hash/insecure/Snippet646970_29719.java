/*
 * Utility class that encrypts a string created for storing passwords
 * into databases
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author mamamambo
 */
public final class Encryptor {
    // Salt for extra security
    private static String salt = "54L7";

    private static String encrptionAlgorithm = "SHA-1";

    private Encryptor() { }

    public static String encrypt(String str) {
        StringBuffer encryptedResult = new StringBuffer();
        String strWithSalt = salt + str;

        try {
            MessageDigest md = MessageDigest.getInstance(encrptionAlgorithm);
            md.update(strWithSalt.getBytes());
            byte[] digest = md.digest();

            for (byte b : digest) {
                int number = b;
                // Convert negative numbers
                number = (number < 0) ? (number + 256) : number;
                encryptedResult.append(Integer.toHexString(number));
            }

        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.toString());
        }

        return encryptedResult.toString();
    }
}
