import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class crypto {
    public static void main(String [] args) {
        String s = args[0];
        String s1 = args[1];
        String ivkey = "thisisasecretword1";
          byte[] ivraw = ivkey.getBytes();
          SecretKeySpec skeySpec = new SecretKeySpec(ivraw, "AES");
          byte[] iv = { 't','h','i','s','a','s','e','c','r','e','t','w','o','r','d','1' };
      IvParameterSpec ivspec = new IvParameterSpec(iv);

        if (s.equalsIgnoreCase("ENCRYPT")) {
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);
                byte[] encrypted = cipher.doFinal(s1.getBytes());
                System.out.println("encrypt: " + new String(Base64.encodeBase64(encrypted)));

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
                byte[] encrypted = cipher.doFinal(Base64.decodeBase64(s1));
                System.out.println("decrypt: " + new String(encrypted));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
