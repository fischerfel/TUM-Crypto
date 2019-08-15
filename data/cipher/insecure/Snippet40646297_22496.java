import java.io.*;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

public class PHPDESEncrypt {
    String key;
    public PHPDESEncrypt() {

    }
    public PHPDESEncrypt(String key) {
        this.key = key;
    }

    public byte[] desEncrypt(byte[] plainText) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        byte data[] = plainText;
        byte encryptedData[] = cipher.doFinal(data);
        return encryptedData;
    }

    public String encrypt(String input) throws Exception {
        return base64Encode(desEncrypt(input.getBytes())).replaceAll("\\s*", "");
    }

    public String base64Encode(byte[] s) {
        if (s == null) return null;
        BASE64Encoder b = new BASE64Encoder();
        return b.encode(s);
    }

    public static void main(String args[]) {
        try {
            PHPDESEncrypt d = new PHPDESEncrypt(args[0]);
            String p=d.encrypt(args[1]);
            System.out.println(p);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
