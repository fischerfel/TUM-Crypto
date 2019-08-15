import java.math.BigInteger;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Blowfish1 {

    public static void main(String[] args) throws Exception {
        String s = "testings";
        Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        Key key = new SecretKeySpec("6#26FRL$ZWD".getBytes(), "Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] enc_bytes = cipher.doFinal(s.getBytes());
        System.out.printf("%x%n", new BigInteger(1, enc_bytes));
    }

}
