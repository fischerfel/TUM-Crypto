        public static String[] SHA512(String password)
{
    //Generates the salt
    SecureRandom saltRandomizer = new SecureRandom();
    byte[] salt = new byte[64]; //The same size as the output of SHA-512 (512 bits = 64 bytes)
    saltRandomizer.nextBytes(salt);
    String encodedSalt = Base64.encodeToString(salt, Base64.DEFAULT);


    //Prepends the salt to the password
    String saltedPassword = encodedSalt + password;

    //Hashed the salted password using SHA-512
    MessageDigest digester;
    byte[] digest = null;
    try {
        digester = MessageDigest.getInstance("SHA-512");
        digester.reset();
        digester.update(saltedPassword.getBytes());
        digest = digester.digest();
    } catch (NoSuchAlgorithmException e) {
        System.out.println("No such algorithm");
        e.printStackTrace();
    }

    String[] passwordPlusSalt = new String[2];
    passwordPlusSalt[0] = Base64.encodeToString(digest, Base64.DEFAULT);
    passwordPlusSalt[1] = encodedSalt;
    return passwordPlusSalt;
}
