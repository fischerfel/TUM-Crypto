import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypt {

    private Crypt() {
    }

    public static String encrypt(String key, String toEncrypt) {
        byte[] toEncryptBytes = toEncrypt.getBytes();
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
            byte[] encrypted = cipher.doFinal(toEncryptBytes);
            return new String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String key, String toDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, getKey(key));
            byte[] decrypted = cipher.doFinal(toDecrypt.getBytes());
            return new String(decrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static SecretKeySpec getKey(String str) {
        byte[] bytes = str.getBytes();
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            bytes = sha.digest(bytes);
            bytes = Arrays.copyOf(bytes, 16);
            return new SecretKeySpec(bytes, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final void main(String[] args) {
        String key = "KEY123";
        String toEncrypt = "0";
        boolean[] b = new boolean[128];
        for (int i = 0; i < 128; i++) {
            if (test(key, toEncrypt) == false) {
                System.err.println("ERROR with size " + toEncrypt.length());
            } else {
                b[i] = true;
            }
            toEncrypt += "0";
        }

        System.out.println("Following sizes don't work:");
        for (int i = 0; i < b.length; i++) {
            if (!b[i]) {
                System.out.println(i + 1);
            }
        }
    }

    // true = success
    public static boolean test(String key, String toEncrypt) {
        try {
            System.out.print(toEncrypt.length() + ": ");
            String encrypted = encrypt(key, toEncrypt);
            System.out.print(encrypted + "; ");

            String decrypted = decrypt(key, encrypted);
            System.out.println(decrypted);

            if (decrypted == null) {
                return false;
            }

            if (toEncrypt.equals(decrypted)) {
                return true;
            }
            return false;
        } catch (Throwable t) {
            return false;
        }
    }
}
