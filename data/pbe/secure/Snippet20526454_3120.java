public static byte[] getEncryptedAnswer(String answer, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    KeySpec spec = new PBEKeySpec(answer.toCharArray(), salt, 20000, 160);

    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

    return secretKeyFactory.generateSecret(spec).getEncoded();
}
