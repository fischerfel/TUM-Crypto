import java.security.MessageDigest;

/**
 * Use to encrypt passwords using MD5 algorithm
 * @param password should be a plain text password.
 * @return a hex String that results from encrypting the given password.
 */
public static String encryptPassword(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<byteData.length;i++) {
            String hex=Integer.toHexString(0xff & byteData[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    catch(java.security.NoSuchAlgorithmException missing) {
        return "Error.";
    }
}
