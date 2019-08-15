public static byte[] getHash(String password) throws NoSuchAlgorithmException {

    MessageDigest digest = MessageDigest.getInstance("SHA-256");

    byte[] passBytes = password.getBytes();

    byte[] salt = digest.digest(passBytes);     

    byte[] digestable = new byte[passBytes.length + salt.length];

    System.arraycopy(passBytes, 0, digestable, 0, passBytes.length);
    System.arraycopy(salt, 0, digestable, passBytes.length, salt.length);

    return digest.digest(digestable);
}
