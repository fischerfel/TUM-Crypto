import java.security.MessageDigest;

public class TestSHA256 {

    public static void main(String[] args) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest("A".getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();
            for (int i: hash) {
                hexString.append(Integer.toHexString(0XFF & i));
            }
            System.out.println(hexString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
