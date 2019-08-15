import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class UUIDTest {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        long currentTimeMillis = System.currentTimeMillis();
        UUID randomUUID = UUID.randomUUID();

        String uuid = randomUUID.toString() + "-" + currentTimeMillis ;
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(uuid.getBytes());
        byte[] mb = md.digest();
        String out = "";

        for (int i = 0; i < mb.length; i++) {
            byte temp = mb[i];
            String s = Integer.toHexString(new Byte(temp));
            while (s.length() < 2) {
                s = "0" + s;
            }
            s = s.substring(s.length() - 2);
            out += s;
        }

        System.out.println(out);
    }

}
