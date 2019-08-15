import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class AES {
    public static String asHex(byte[] buf) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
            if (((int)buf[i] & 0xff) < 0x10)
                strbuf.append("0");
            strbuf.append(Long.toString((int)buf[i] & 0xff, 16));
        }

        return strbuf.toString();
    }

    public static void main(String[] args) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128); // 192 and 256 bits may not be available
        // Generate the secret key specs.
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        String target = "This is just an example";
        StringTokenizer token = new StringTokenizer(target);
        while(token.hasMoreTokens()) {
            String temp = token.nextToken();
            byte[] encrypted = cipher.doFinal((args.length == 0 ?  temp : args[0]).getBytes());
            System.out.println(asHex(encrypted) + " ");
        }
    }
}
