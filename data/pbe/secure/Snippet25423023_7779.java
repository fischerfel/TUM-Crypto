public class PBKDF2WithSha256DatabaseServerLoginModule extends DatabaseServerLoginModule {
    protected boolean validatePassword(String inputPassword, String expectedPassword) {
        if(inputPassword == null || expectedPassword == null) {
            return false;
        }
        String[] encodedPassword = expectedPassword.split("\\$");
        int encodedIterations = Integer.parseInt(encodedPassword[1]);
        byte[] encodedSalt = encodedPassword[2].getBytes(Charset.forName("UTF-8"));
        String encodedHash = encodedPassword[3];
        SecretKeyFactory f = null;
        try {
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Need a Java implementation with cryptography.");
        }
        KeySpec ks = new PBEKeySpec(inputPassword.toCharArray(), encodedSalt, encodedIterations, 256);
        SecretKey s = null;
        try {
            s = f.generateSecret(ks);
        } catch (InvalidKeySpecException e) {
            // Encoded password is corrupt
            return false;
        }
        if (encodedHash.equals(Base64.getEncoder().encodeToString(s.getEncoded()))) {
            return true;
        } else {
            return false;
        }
    }
}
