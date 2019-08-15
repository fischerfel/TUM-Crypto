public SecretKey createKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
{
        //hash the password
        MessageDigest msgDigest = MessageDigest.getInstance("SHA-1");
        msgDigest.update(key);
        byte[] hash = msgDigest.digest();

        byte[] salt = new byte[8]{};
        new SecureRandom(hash).nextBytes(salt);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(hash, salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(keySpec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        return secret;
}
