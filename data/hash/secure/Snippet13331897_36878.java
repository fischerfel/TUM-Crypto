import java.security.NoSuchAlgorithmException;

public class hash {

private static final int DRUPAL_HASH_LENGTH = 55;

private static String _password_itoa64() {
    return "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
}

public static void main(String args[]) throws Exception {
    // Passwords and hashes generated by Drupal.
    checkPassword("adrian", "$S$DNbBTrkalsPChLsqajHUQS18pBBxzSTQW0310SzivTy7HDQ.zgyG");
    checkPassword("test"  , "$S$DxVn7wubSRzoK9X2pkGx4njeDRkLEgdqPphc2ZXkkb8Viy8JEGf3");
    checkPassword("barbaz", "$S$DOASeKfBzZoqgSRl/mBnK06GlLESyMHZ81jyUueEBiCrkkxxArpR");
}


private static int password_get_count_log2(String setting) {
    return _password_itoa64().indexOf(setting.charAt(3));
}


private static byte[] sha512(String input) {
    try {
        return java.security.MessageDigest.getInstance("SHA-512").digest(input.getBytes());
    } catch (NoSuchAlgorithmException ex) {
        ex.printStackTrace();
    }
    return new byte[0];
}

private static byte[] sha512(byte[] input) {
    try {
        return java.security.MessageDigest.getInstance("SHA-512").digest(input);
    } catch (NoSuchAlgorithmException ex) {
        ex.printStackTrace();
    }
    return new byte[0];
}

/**
 * Note: taken from the default Drupal 7 password algorithm
 *
 * @param candidate               the clear text password
 * @param saltedEncryptedPassword the salted encrypted password string to check => NEEDS TO BE THE DEFAULT DRUPAL 7 PASSWORD HASH.
 * @return true if the candidate matches, false otherwise.
 */
public static boolean checkPassword(String candidate, String saltedEncryptedPassword) throws Exception {
    if (candidate == null || saltedEncryptedPassword == null) {
        return false;
    }

    String hash = password_crypt(candidate, saltedEncryptedPassword);
    System.out.println("Expected value = " + saltedEncryptedPassword);
    System.out.println("Calced   value = " + hash);
    System.out.println("Result Good?   = " + saltedEncryptedPassword.equalsIgnoreCase(hash));


    return saltedEncryptedPassword.equalsIgnoreCase(hash);
}


private static String password_crypt(String password, String passwordHash) throws Exception {
    // The first 12 characters of an existing hash are its setting string.
    passwordHash = passwordHash.substring(0, 12);
    int count_log2 = password_get_count_log2(passwordHash);
    String salt = passwordHash.substring(4, 12);
    // Hashes must have an 8 character salt.
    if (salt.length() != 8) {
        return null;
    }

    int count = 1 << count_log2;


    byte[] hash;
    try {
        hash = sha512(salt.concat(password));

        do {
            hash = sha512(joinBytes(hash, password.getBytes("UTF-8")));
        } while (--count > 0);
    } catch (Exception e) {
        System.out.println("error " + e.toString());
        return null;
    }

    String output = passwordHash + _password_base64_encode(hash, hash.length);
    return (output.length() > 0) ? output.substring(0, DRUPAL_HASH_LENGTH) : null;
}

private static byte[] joinBytes(byte[] a, byte[] b) {
    byte[] combined = new byte[a.length + b.length];

    System.arraycopy(a, 0, combined, 0, a.length);
    System.arraycopy(b, 0, combined, a.length, b.length);
    return combined;
}



private static String _password_base64_encode(byte[] input, int count) throws Exception {

    StringBuffer output = new StringBuffer();
    int i = 0;
    CharSequence itoa64 = _password_itoa64();
    do {
        long value = SignedByteToUnsignedLong(input[i++]);

        output.append(itoa64.charAt((int) value & 0x3f));
        if (i < count) {
            value |= SignedByteToUnsignedLong(input[i]) << 8;
        }
        output.append(itoa64.charAt((int) (value >> 6) & 0x3f));
        if (i++ >= count) {
            break;
        }
        if (i < count) {
            value |=  SignedByteToUnsignedLong(input[i]) << 16;
        }

        output.append(itoa64.charAt((int) (value >> 12) & 0x3f));
        if (i++ >= count) {
            break;
        }
        output.append(itoa64.charAt((int) (value >> 18) & 0x3f));
    } while (i < count);

    return output.toString();
}


public static long SignedByteToUnsignedLong(byte b) {
    return b & 0xFF;
}

}
