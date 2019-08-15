import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;


public class EncryptAES {

    private static String toHexString(byte[] data) {        
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; ++i) {
            String s = Integer.toHexString(data[i] & 0XFF);
            buf.append((s.length() == 1) ? ("0" + s) : s);
        }
        return buf.toString();
    }

    public static String encrypt(String input, String key) {
        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
            final String encryptedString = toHexString(Base64.encodeBase64(crypted));
            return encryptedString;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return new String(new BASE64Encoder().encode(crypted));
    }

    public static void main(String[] args) {
        String key = args[0];
        String plaintext = args[1];
        System.out.println("KEY = " + key);
        System.out.println("PLAINTEXT = " + plaintext);
        System.out.println("CIPHER = " + EncryptAES.encrypt(plaintext, key));
    }
}
