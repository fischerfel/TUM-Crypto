package blowfish;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Blowfish {

public static void main(String[] args) throws Exception {
    encrypt("1234","some data");
    decrypt("1234","A+oDE+RfTx11nT3iGgUvCw==");
}

private static void encrypt(String key, String string) throws Exception {

        byte[] keyData = (key).getBytes();
        //System.out.println(keyData);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] hasil = cipher.doFinal(string.getBytes());
        System.out.println(new BASE64Encoder().encode(hasil));
    }

private static void decrypt(String key, String string) throws Exception {
        byte[] keyData = (key).getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] hasil = cipher.doFinal(new BASE64Decoder().decodeBuffer(string));
        System.out.println(new String(hasil));
    }
}
