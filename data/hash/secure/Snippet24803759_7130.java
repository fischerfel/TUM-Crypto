/**
 * Same security, twice the speed.
 */
public static String generateStorngPasswordHashWithSHA256(String password) {
    try {
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, 1010101,
                20 * Byte.SIZE);
        SecretKeyFactory skf = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();

        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        hash = sha256.digest();

        return toHex(salt) + ":" + toHex(hash);
    } catch (Exception e) {
        System.out.println("Exception: Error in generating password"
                + e.toString());
    }
    return "";
}
