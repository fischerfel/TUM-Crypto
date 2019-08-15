public static String generatePasswordHash(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException
{
    char[] chars = password.toCharArray();
    byte[] saltBytes =salt.getBytes(StandardCharsets.US_ASCII);

    PBEKeySpec spec = new PBEKeySpec(chars, saltBytes, 10000, 256);
    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    byte[] hash = skf.generateSecret(spec).getEncoded();
    BigInteger bi = new BigInteger(1, hash);
    return String.format("%064x", bi);
}
