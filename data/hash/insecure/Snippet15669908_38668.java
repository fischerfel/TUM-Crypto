import java.security.MessageDigest;

public class TestHash {

    public static void main(String[] args) throws Exception {
        String password = "9a";

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(password.getBytes());
        byte[] byteData = md.digest();
        // byte[] byteData = md.digest(password.getBytes());    // both updates and completes the hash computation

        // Method 1 of converting bytes to hex format
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xFF) + 0x100, 16).substring(1));
        }

        System.out.println("1) Hex format : " + sb.toString());

        // Method 2 of converting bytes to hex format
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            // NB! E.g.: Integer.toHexString(0x0C) will return "C", not "0C"            
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        System.out.println("2) Hex format : " + hexString.toString());      
    }
}
