import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;


public class testEncrypt {
    public static void main(String[] args) throws Exception {
        String data = "Welcome2012~1@Welcome2012~1@Welcome2012~1@Welcome2012~1@Welcome2012~1@";
        String sEncryptionKey = "encryption key"; # the same key
        byte[] rawData = new Base64().decode(data);
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        Rfc2898DeriveBytes keyGen = new Rfc2898DeriveBytes(sEncryptionKey, salt);

        byte[] IV = keyGen.getBytes(128 / 8);
        byte[] keyByte = keyGen.getBytes(256 / 8);

        Key key = new SecretKeySpec(keyByte, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV));
        byte[] out2 = cipher.doFinal(rawData);

        byte[] out = new byte[8 + out2.length];
        System.arraycopy(salt, 0, out, 0, 8);
        System.arraycopy(out2, 0, out, 8, out2.length);
        //String outStr=new String(out,"UTF-8");
        String outStr = new Base64().encodeToString(out);
        System.out.println(outStr);
        System.out.print(outStr.length());

    }
}
