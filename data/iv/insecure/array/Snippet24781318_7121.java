import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES_CBC {

    public static void main(String[] args) throws Exception {

    byte[] keyBytes = new byte[] { 14, (byte) 0x0b, (byte) 0x41,
            (byte) 0xb2, (byte) 0x2a, (byte) 0x29, (byte) 0xbe,
            (byte) 0xb4, (byte) 0x06, (byte) 0x1b, (byte) 0xda,
            (byte) 0x66, (byte) 0xb6, (byte) 0x74, (byte) 0x7e, (byte) 0x14 };

    byte[] ivBytes = new byte[] { (byte) 0x4c, (byte) 0xa0, (byte) 0x0f,
            (byte) 0xf4, (byte) 0xc8, (byte) 0x98, (byte) 0xd6,
            (byte) 0x1e, (byte) 0x1e, (byte) 0xdb, (byte) 0xf1,
            (byte) 0x80, (byte) 0x06, (byte) 0x18, (byte) 0xfb, (byte) 0x28 };

    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    byte[] cipherText = new byte[] { (byte) 0x28, (byte) 0xa2, (byte) 0x26,
            (byte) 0xd1, (byte) 0x60, (byte) 0xda, (byte) 0xd0,
            (byte) 0x78, (byte) 0x83, (byte) 0xd0, (byte) 0x4e,
            (byte) 0x00, (byte) 0x8a, (byte) 0x78, (byte) 0x97,
            (byte) 0xee, (byte) 0x2e, (byte) 0x4b, (byte) 0x74,
            (byte) 0x65, (byte) 0xd5, (byte) 0x29, (byte) 0x0d,
            (byte) 0x0c, (byte) 0x0e, (byte) 0x6c, (byte) 0x68,
            (byte) 0x22, (byte) 0x23, (byte) 0x6e, (byte) 0x1d,
            (byte) 0xaa, (byte) 0xfb, (byte) 0x94, (byte) 0xff,
            (byte) 0xe0, (byte) 0xc5, (byte) 0xda, (byte) 0x05,
            (byte) 0xd9, (byte) 0x47, (byte) 0x6b, (byte) 0xe0,
            (byte) 0x28, (byte) 0xad, (byte) 0x7c, (byte) 0x1d, (byte) 0x81 };

    cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
    byte[] original = cipher.doFinal(cipherText);
    String plaintext = new String(original);
    System.out.println(plaintext);
}
}
