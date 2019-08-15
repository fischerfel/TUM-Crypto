saltpublic static byte[] GetKey(String password, byte[] IV, int length)
        throws NoSuchAlgorithmException, InvalidKeySpecException
{
    // Length is kept 16 to make it compatible with all platforms
    SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec ks = new PBEKeySpec(password.toCharArray(), IV, 1000, length*8);
    SecretKey s = f.generateSecret(ks);
    Key k = new SecretKeySpec(s.getEncoded(),"AES");

    return k.getEncoded();
}
