import java.security.MessageDigest;
import java.util.Formatter;

class Main{

    public static String calculateHash(String password) throws Exception{
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");

        String encodedPassword = "S:71752CE0530476A8B2E0DD218AE59CB71B211D7E1DB70EE23BFB23BDFD48";

        // Convert password to bytes
        byte[] bPassword = password.getBytes("UTF-8");

        // Get salt from encoded password
        String salt = encodedPassword.substring(42, 62);
        System.out.println("Salt is " + salt);

        // Convert salt from hex back to bytes
        // based on http://stackoverflow.com/a/140861/266304
        int len = salt.length();
        byte[] bSalt = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bSalt[i / 2] = (byte) ((Character.digit(salt.charAt(i), 16) << 4)
                + Character.digit(salt.charAt(i+1), 16));
        }

        // Add converted salt to password bytes
        // based on http://stackoverflow.com/a/80503/266304
        byte[] bData = new byte[bPassword.length + bSalt.length];
        System.arraycopy(bPassword, 0, bData, 0, bPassword.length);
        System.arraycopy(bSalt, 0, bData, bPassword.length, bSalt.length);

        // Hash the final byte array
        crypt.update(bData);
        byte bHash[] = crypt.digest();

        Formatter formatter = new Formatter();
        for (byte b : bHash)
        {
            formatter.format("%02x", b);
        }

        System.out.println("Expected      " + encodedPassword.substring(2,42));

        return formatter.toString().toUpperCase();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("The result is " + calculateHash("ZK3002"));
    }
}
