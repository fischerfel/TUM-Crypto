import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MB5Returner extends Thread {

    private int startPoint;
    private int endPoint;

    public void run() {
        for (int i = startPoint; i < endPoint; i++) {
            try {
                String val = Words.allPossible.get(i);
                MessageDigest m;

                m = MessageDigest.getInstance("MD5");

                m.update(val.getBytes(), 0, val.length());
                String hashed = new BigInteger(1, m.digest()).toString(16);

            //  System.out.println("MD5 = " + hashed);
                checkMD5(hashed, val);

            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FoundMD5Exception e) {

            }
        }
    }

    private void checkMD5(String hashed, String val) throws FoundMD5Exception {
        if (hashed.equals(Main.hashedPassword)) {
            throw new FoundMD5Exception(hashed, val);
        }
    }

    public MB5Returner(int startPoint, int endPoint) {
        super();
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

}
