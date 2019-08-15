import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Main{
    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        byte [] plain = "I eat fish every day".getBytes("utf-8");

        byte [] keyBytes = new byte [] { (byte) 0xC1, (byte) 0x57, (byte) 0x45, (byte) 0x08,
            (byte) 0x85, (byte) 0x02, (byte) 0xB0, (byte) 0xD3,
            (byte) 0xA2, (byte) 0xEF, (byte) 0x68, (byte) 0x43,
            (byte) 0x5E, (byte) 0xE6, (byte) 0xD0, (byte) 0x75 };


        SecretKey keySpec = new SecretKeySpec(keyBytes, "DESede");

        IvParameterSpec iv = new IvParameterSpec(new byte[8]);

        Cipher e_cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");

        e_cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

        byte [] cipherText = e_cipher.doFinal(plain);

        System.out.println("Ciphertext: " + new sun.misc.BASE64Encoder().encode(cipherText));
    }
}
