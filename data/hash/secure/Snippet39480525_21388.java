import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class PhpassHashedPassword {

    private static final int DRUPAL_HASH_LENGTH = 55;

    private static final String ITOA_64 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static int passwordGetCount(String setting) {
        return ITOA_64.indexOf(setting.charAt(3));
    }

    private static byte[] sha512(byte[] input) throws NoSuchAlgorithmException {
        return java.security.MessageDigest.getInstance("SHA-512").digest(input);
    }

    /**
     * Note: taken from the default Drupal 7 password algorithm
     *
     * @param candidate the clear text password
     * @param saltedEncryptedPassword the salted encrypted password string to
     * check => NEEDS TO BE THE DEFAULT DRUPAL 7 PASSWORD HASH.
     * @return true if the candidate matches, false otherwise.
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     */
    public static boolean validatePasswordHash(String candidate, String saltedEncryptedPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (candidate == null || saltedEncryptedPassword == null) {
            return false;
        }

        String hash = password_crypt(candidate, saltedEncryptedPassword);
        return saltedEncryptedPassword.equalsIgnoreCase(hash);
    }

    private static String password_crypt(String password, String passwordHash) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // The first 12 characters of an existing hash are its setting string.
        passwordHash = passwordHash.substring(0, 12);
        int count_log2 = passwordGetCount(passwordHash);
        String salt = passwordHash.substring(4, 12);
        // Hashes must have an 8 character salt.
        if (salt.length() != 8) {
            return null;
        }

        int count = 1 << count_log2;

        byte[] hash = sha512(salt.concat(password).getBytes());

        do {
            hash = sha512(joinBytes(hash, password.getBytes("UTF-8")));
        } while (--count > 0);

        String output = passwordHash + passwordBase64Encode(hash, hash.length);
        return (output.length() > 0) ? output.substring(0, DRUPAL_HASH_LENGTH) : null;
    }

    private static byte[] joinBytes(byte[] a, byte[] b) {
        byte[] combined = new byte[a.length + b.length];

        System.arraycopy(a, 0, combined, 0, a.length);
        System.arraycopy(b, 0, combined, a.length, b.length);
        return combined;
    }

    private static String passwordBase64Encode(byte[] input, int count) {

        StringBuilder output = new StringBuilder();
        int i = 0;
        do {
            long value = signedByteToUnsignedLong(input[i++]);

            output.append(ITOA_64.charAt((int) value & 0x3f));
            if (i < count) {
                value |= signedByteToUnsignedLong(input[i]) << 8;
            }
            output.append(ITOA_64.charAt((int) (value >> 6) & 0x3f));
            if (i++ >= count) {
                break;
            }
            if (i < count) {
                value |= signedByteToUnsignedLong(input[i]) << 16;
            }

            output.append(ITOA_64.charAt((int) (value >> 12) & 0x3f));
            if (i++ >= count) {
                break;
            }
            output.append(ITOA_64.charAt((int) (value >> 18) & 0x3f));
        } while (i < count);

        return output.toString();
    }

    private static long signedByteToUnsignedLong(byte b) {
        return b & 0xFF;
    }

}
