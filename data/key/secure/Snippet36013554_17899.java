    private SecretKey deriveKey(char[] password, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {

       SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
       KeySpec keySpec = new PBEKeySpec(password, salt, 65536, 256);
       SecretKey tempSecretKey = secretKeyFactory.generateSecret(keySpec);
       SecretKey secretKey = new SecretKeySpec(tempSecretKey.getEncoded(), "AES");

       return secretKey;
}
