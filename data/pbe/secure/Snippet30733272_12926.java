/**
 * Creates the salted hash.
 *
 * @param password
 *            the password
 * @return the map
 */
@SuppressWarnings("unused")
private static Map<byte[], byte[]> createSaltedHash(String password) {

    Map<byte[], byte[]> saltedHash = new HashMap<byte[], byte[]>();
    byte[] hash = null;
    byte[] salt = null;
    final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    // The following may be changed without breaking existing hashes.
    final int SALT_BYTE_SIZE = 24;
    final int HASH_BYTE_SIZE = 24;
    final int PBKDF2_ITERATIONS = 1000;
    final int ITERATION_INDEX = 0;
    final int SALT_INDEX = 1;
    final int PBKDF2_INDEX = 2;

    SecureRandom secureRandom = new SecureRandom();
    salt = new byte[SALT_BYTE_SIZE];
    secureRandom.nextBytes(salt);
    //byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);

    PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt,
            PBKDF2_ITERATIONS, (HASH_BYTE_SIZE * 8));
    try {
        SecretKeyFactory skf = SecretKeyFactory
                .getInstance(PBKDF2_ALGORITHM);
        hash = skf.generateSecret(spec).getEncoded();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    }

    System.out.println("HASH:" + hash); // Store this in DB
    System.out.println("SALT:" + salt); // Store this in DB
    saltedHash.put(hash, salt);
    return saltedHash;
}
