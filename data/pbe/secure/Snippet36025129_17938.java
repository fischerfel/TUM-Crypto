public String hashPassword(String password, byte[] salt){

 char[] passwordChars = password.toCharArray();

     PBEKeySpec spec = new PBEKeySpec(
         passwordChars,
         salt,
         ITERATIONS,
         KEY_LENGTH
     );
     SecretKeyFactory key = null;
    try {
        key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }

    byte[] hashedPassword = null;

    try {
        hashedPassword = key.generateSecret(spec).getEncoded();
    } catch (InvalidKeySpecException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
     return String.format("%x", new BigInteger(hashedPassword));

}
