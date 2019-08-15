import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

private static final int SALT_LENGTH = 4;

public static String generateSSHA(byte[] password)
        throws NoSuchAlgorithmException {
    SecureRandom secureRandom = new SecureRandom();
    byte[] salt = new byte[SALT_LENGTH];
    secureRandom.nextBytes(salt);

    MessageDigest crypt = MessageDigest.getInstance("SHA-1");
    crypt.reset();
    crypt.update(password);
    crypt.update(salt);
    byte[] hash = crypt.digest();

    byte[] hashPlusSalt = new byte[hash.length + salt.length];
    System.arraycopy(hash, 0, hashPlusSalt, 0, hash.length);
    System.arraycopy(salt, 0, hashPlusSalt, hash.length, salt.length);

    return new StringBuilder().append("{SSHA}")
            .append(Base64.getEncoder().encodeToString(hashPlusSalt))
            .toString();
}
