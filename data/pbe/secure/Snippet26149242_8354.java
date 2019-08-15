public abstract class PasswordServiceImpl implements PasswordByteService {

private String ENCRIPT_ALGORITHM = "PBKDF2WithHmacSHA1";
private int ENCRYPT_DEVIRED_KEY_LENGTH = 160;
private int ENCRIPT_ITERATIONS = 20000;
private String SALT_ALGORITHM = "SHA1PRNG";


@Override
    public boolean authenticate(final String attemptedPassword, final byte[] encryptedPassword, final byte[] salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException {

    byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);

    return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
}


@Override
public byte[] getEncryptedPassword(final String password, byte[] salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException {

    String algorithm = ENCRIPT_ALGORITHM;
    int derivedKeyLength = ENCRYPT_DEVIRED_KEY_LENGTH;
    int iterations = ENCRIPT_ITERATIONS;

    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);

    SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
    byte[] encoded = f.generateSecret(spec).getEncoded();


    return encoded;
}


@Override
public byte[] generateSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
    SecureRandom random;
        random = SecureRandom.getInstance(SALT_ALGORITHM,"SUN");

    byte[] salt = new byte[8];
    random.nextBytes(salt);

    return salt;
}

}
