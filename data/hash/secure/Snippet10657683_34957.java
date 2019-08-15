public byte[] generateSalt() throws NoSuchAlgorithmException {
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[20];
    random.nextBytes(salt);
    return salt;
}

public byte[] generateHash(byte[] salt, String pass) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-512");
    digest.update(salt);
    byte[] hash = digest.digest(pass.getBytes());
    return hash;
}
