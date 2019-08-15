import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {

    /**
     * @param args
     */

    public static void main(String[] args) throws Exception {

        appGUI gui = new appGUI();
        gui.setVisible(true);

        String password = ""; //The password entered...
        try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        System.out.println(new BigInteger(1, md.digest()).toString(16));

        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    }
