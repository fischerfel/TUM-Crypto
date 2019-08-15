private SecretKey generateKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    // Number of PBKDF2 hardening rounds to use. Larger values increase computation time. You
    // should select a value that causes computation to take >100ms.
    final int iterations = 1000;

    // Generate a 256-bit key
    final int outputKeyLength = 256;

    SecretKeyFactory secretKeyFactory;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        // Use compatibility key factory -- only uses lower 8-bits of passphrase chars
        secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1And8bit");
    }
    else {
        // Traditional key factory. Will use lower 8-bits of passphrase chars on
        // older Android versions (API level 18 and lower) and all available bits
        // on KitKat and newer (API level 19 and higher).
        secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    }

    KeySpec keySpec = new PBEKeySpec(password, salt, iterations, outputKeyLength);

    return secretKeyFactory.generateSecret(keySpec);
}
