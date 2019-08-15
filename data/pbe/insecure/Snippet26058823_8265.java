public static SecretKey generateKey() throws NoSuchAlgorithmException {

    char[] password = { 'a', 'b', 'c', 'd', 'e' };
    byte[] salt = { 1, 2, 3, 4, 5 };

    SecretKeyFactory factory = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
    SecretKey tmp = null;
    try {
        tmp = factory.generateSecret(spec);
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    }

    yourKey = new SecretKeySpec(tmp.getEncoded(), "AES");

    return yourKey;
}
