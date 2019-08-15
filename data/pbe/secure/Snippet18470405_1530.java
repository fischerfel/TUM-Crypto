public byte[] encrypt(String clearPassword, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    System.out.println(clearPassword+"     **********     "+salt);
    String algorithm = "PBKDF2WithHmacSHA1";
    int derivedKeyLength = 1600;
    int iterations = 20000;

    KeySpec spec = new PBEKeySpec(clearPassword.toCharArray(), salt, iterations, derivedKeyLength);
    SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
    byte [] truc = f.generateSecret(spec).getEncoded();
    System.out.println(truc);
    return truc;
}

public byte[] generateSalt() throws NoSuchAlgorithmException {
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[8];
    random.nextBytes(salt);

    return salt;
}
