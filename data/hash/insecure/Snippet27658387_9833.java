/**
 * Utility class for MD5 management.
 *
 * @author Massimo Carli - Jul 12, 2013
 */
public final class MD5Utility {

    /*
     * The FF value.
     */
    private static final int FF_HEX_VALUE = 0xFF;

    // Private constructor
    private MD5Utility() {
        throw new AssertionError("Never instantiate this!");
    }

    /**
     * Calculate MD5 for String src.
     *
     * @param src The string to check
     * @return MD5 for src or null if some error occured
     */
    public static String md5(String src) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(src.getBytes());
            byte[] messageDigest = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(FF_HEX_VALUE & messageDigest[i]));
            }
            String md5 = hexString.toString();
            return md5;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Check a String with its MD5.
     *
     * @param str The string
     * @param md5 The MD5 to check
     * @return true if ok, false otherwise
     */
    public static boolean checkMD5(String str, String md5) {
        String pwdMD5 = md5(str);
        // We return the test
        boolean success = md5.equals(pwdMD5);
        return success;
    }

}
