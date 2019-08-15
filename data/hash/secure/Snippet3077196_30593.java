public static boolean authenticate(String salt, String encryptedPassword, 
    char[] plainTextPassword ) throws NoSuchAlgorithmException {

    // do I need to explcitly specify character encoding here? -->
    String saltPlusPlainTextPassword = salt + new String(plainTextPassword);

    MessageDigest sha = MessageDigest.getInstance("SHA-512");

    // is this machine dependent? -->
    sha.update(saltPlusPlainTextPassword.getBytes());
    byte[] hashedByteArray = sha.digest();

    // or... perhaps theres a translation problem here? -->
    String hashed = new String(hashedByteArray);

    return hashed.equals(encryptedPassword);
}
