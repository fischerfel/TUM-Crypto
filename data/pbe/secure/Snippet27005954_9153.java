public static boolean validatePassword(String password, String correctHash) throws NoSuchAlgorithmException, InvalidKeySpecException {
return validatePassword(password.toCharArray(), correctHash);
}

public static boolean validatePassword(char[] password, String correctHash)
    throws NoSuchAlgorithmException, InvalidKeySpecException {
// Decode the hash into its parameters
String[] params = correctHash.split(":");
int iterations = Integer.parseInt(params[ITERATION_INDEX]);
byte[] salt = Base64.decodeBase64(params[SALT_INDEX]);
byte[] hash = Base64.decodeBase64(params[PBKDF2_INDEX]);
// Compute the hash of the provided password, using the same salt,
// iteration count, and hash length
byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
// Compare the hashes in constant time. The password is correct if
// both hashes match.
return slowEquals(hash, testHash);
}

private static boolean slowEquals(byte[] a, byte[] b) {
int diff = a.length ^ b.length;
for (int i = 0; i < a.length && i < b.length; i++)
    diff |= a[i] ^ b[i];
return diff == 0;
}

private static byte[] pbkdf2(char[] password, byte[] salt, int iterations,
    int bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
return skf.generateSecret(spec).getEncoded();
}
