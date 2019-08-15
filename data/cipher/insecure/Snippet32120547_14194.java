import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class CryptoUtils {
    private static final String AES = "AES";
//  private static byte[] keyValue = new byte[]     // OK 
//          { 'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };
    private static byte[] keyValue = new byte[]     // FAILS !!! WTF!
            { 'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'z' };

    public static String encrypt(String Data) throws Exception {
        Key key = new SecretKeySpec(keyValue, AES);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        return new BASE64Encoder().encode(encVal);
    }

    public static String decrypt(String encryptedData) throws Exception {
        Key key = new SecretKeySpec(keyValue, AES);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(CryptoUtils.encrypt("<PASSWORD>"));
        System.out.println(CryptoUtils.decrypt("Z4i3ywGXil2QCfM6R8S5qw=="));
    }
}
