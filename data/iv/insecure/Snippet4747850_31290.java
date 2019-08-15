import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;

/**
 * Decrypt passwords stored in Oracle SQL Developer. This is intended for
 * password recovery.
 * 
 * Passwords are stored in
 * ~/.sqldeveloper/system2.1.1.64.39/o.jdeveloper.db.connection
 * .11.1.1.2.36.55.30/connections.xml
 */
public class Decrypt {
    public static byte[] decryptPassword(byte[] result)
            throws GeneralSecurityException {
        byte constant = result[0];
        if (constant != (byte) 5) {
            throw new IllegalArgumentException();
        }

        byte[] secretKey = new byte[8];
        System.arraycopy(result, 1, secretKey, 0, 8);

        byte[] encryptedPassword = new byte[result.length - 9];
        System.arraycopy(result, 9, encryptedPassword, 0,
                encryptedPassword.length);

        byte[] iv = new byte[8];
        for (int i = 0; i < iv.length; i++) {
            iv[i] = 0;
        }

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey, "DES"),
                new IvParameterSpec(iv));
        return cipher.doFinal(encryptedPassword);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage:  java Decrypt <password>");
            System.exit(1);
        }

        if (args[0].length() % 2 != 0) {
            System.err
                    .println("Password must consist of hex pairs.  Length is odd (not even).");
            System.exit(2);
        }

        byte[] secret = new byte[args[0].length() / 2];
        for (int i = 0; i < args[0].length(); i += 2) {
            String pair = args[0].substring(i, i + 2);
            secret[i / 2] = (byte) (Integer.parseInt(pair, 16));
        }

        try {
            System.out.println(new String(decryptPassword(secret)));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            System.exit(3);
        }
    }
}
