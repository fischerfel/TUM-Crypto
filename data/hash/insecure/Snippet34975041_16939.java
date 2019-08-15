import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) {
        String xmlRequest = " <request><merchant_id>46</merchant_id><order_id>33</order_id><amount>3</amount><description>hehe</description></request>";
        String result = encode(xmlRequest);
        String lol = "lol";
        String lolResult = encode(lol);
        System.out.println(result);
        System.out.println(lolResult);
    }

    public static String encode(String toEncode) {
        String hashStr = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(toEncode.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            hashStr = hash.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashStr;
    }
}
