public static byte[] getEncryptedPassword(
    String password,
    byte[] salt,
    int iterations,
    int derivedKeyLength
) throws NoSuchAlgorithmException, InvalidKeySpecException {
    KeySpec keySpec = new PBEKeySpec(
        password.toCharArray(),
        salt,
        iterations,
        derivedKeyLength * 8
    );
    SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    return f.generateSecret(keySpec).getEncoded();
}

@Test
public void testHashing(){
    byte[] salt = new SecureRandomNumberGenerator().nextBytes().getBytes();
    byte[] hash1 = new Sha256Hash("1234", salt, 1024).getBytes();
    byte[] hash2 = getEncryptedPassword("1234", salt, 1024, 32);
    assertTrue(hash1.equals(hash2));
}
