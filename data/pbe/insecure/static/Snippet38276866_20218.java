try {
    byte[] salt = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

    PBEKeySpec keySpec = new PBEKeySpec("password".toCharArray(), salt, 1000, 256);

    SecretKey secretkey = factory.generateSecret(keySpec);
    byte[] key = secretkey.getEncoded();

    // Using key

    // Destroy key
    Arrays.fill(key, (byte)0);

    // Destroy secretKey
    secretkey.destroy();  // --> Throw DestroyFailedException

} catch (Exception e) {
    e.printStackTrace();
}
