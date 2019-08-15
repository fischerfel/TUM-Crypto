import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Main{
    public static void main(String[] args) throws Exception {
        byte [] plain = "I eat fish every day".getBytes("utf-8");

        byte [] keyBytes = new byte [] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
            };

        byte [] key2Bytes = new byte [] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0  }; // actual keys replaced with dummies.

        SecretKey keySpec = new SecretKeySpec(keyBytes, "DES");
        SecretKey keySpec2 = new SecretKeySpec(key2Bytes, "DES");

        IvParameterSpec iv = new IvParameterSpec(new byte[8]);

        Cipher e_cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");

        e_cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec2, iv);

        byte [] cipherText = e_cipher.doFinal(plain);
        cipherText = cipher.doFinal(cipherText);
        cipherText = e_cipher.doFinal(cipherText);

        System.out.println("Ciphertext: " + new sun.misc.BASE64Encoder().encode(cipherText));
    }
}
