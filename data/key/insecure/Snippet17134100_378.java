import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;


class Main
{
    public static void main (String[] args) throws java.lang.Exception
    {
        String s = "testings";
        Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        Key key = new SecretKeySpec("6#26FRL$ZWD".getBytes(), "Blowfish");
        cipher.init(1, key);
        byte[] enc_bytes = cipher.doFinal(s.getBytes());
        System.out.println(enc_bytes);
    }
}
