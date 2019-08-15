public class MD5Helper {

    private static final int MD5_PASSWORD_LENGTH = 16;

    public static String hashPassword(String password) {
        String hashword = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
            BigInteger hash = new BigInteger(1, md5.digest());
            hashword = hash.toString(MD5_PASSWORD_LENGTH);
        } catch (NoSuchAlgorithmException nsae) {
            // handle exception
        }
        return hashword;
    }
}
