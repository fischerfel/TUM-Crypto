package blowfishcbc;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class BlowfishCBC {

    public static void main(String[] args) throws Exception {

        String keyString = "7890";
        String input = "some data";

        Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");

        // for IV
        byte[] iv = new byte[cipher.getBlockSize()];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // for key
        byte[] keyData = (keyString).getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(keyData, "Blowfish");

        // encrypt
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(input.getBytes("UTF-8"));
        String enc = new BASE64Encoder().encode(encrypted);
        System.out.println("encrypted: " + new String(enc));

        // decrypt
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] dec = new BASE64Decoder().decodeBuffer(enc);
        byte[] decrypted = cipher.doFinal(dec);
        System.out.println("decrypted: " + new String(decrypted, "UTF-8"));
    }
}
