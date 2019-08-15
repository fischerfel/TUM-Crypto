// Code below omits comments for Brevity

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import java.math.BigInteger;

public class JBoss {

    public static void main(String[] args) throws Exception {

        if ((args.length != 2)
                || !(args[0].equals("-e") | args[0].equals("-d"))) {
            System.out
                    .println("Usage:\n\tjava JBoss <-e|-d> <encrypted_password>");
            return;
        }

        String mode = args[0];

        byte[] kbytes = "jaas is the way".getBytes();
        SecretKeySpec key = new SecretKeySpec(kbytes, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");

        String out = null;

        if (mode.equals("-e")) {
            String secret = args[1];
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encoding = cipher.doFinal(secret.getBytes());
            out = new BigInteger(encoding).toString(16);
        } else {
            BigInteger secret = new BigInteger(args[1], 16);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encoding = cipher.doFinal(secret.toByteArray());
            out = new String(encoding);
        }
        System.out.println(out);
    }
}
