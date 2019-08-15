package ...

import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class SymmetricDESedeCipher {
    private static final String DATA = "Whackabad";
    private static final String key = "bAJLyifeUJUBFWdHzVbykfDmPHtLKLMzViHW9aHGmyTLD8hGYZ";
    private static final String ALGORITHM = "DESede";
    private static final String XFORM = "DESede/CBC/PKCS5Padding";

    private static byte[] iv = new byte[8];

    private static byte[] encrypt(byte[] inpBytes,
                                  SecretKey key, String XFORM) throws Exception {
        Cipher cipher = Cipher.getInstance(XFORM);
        IvParameterSpec ips = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ips);
        return cipher.doFinal(inpBytes);
    }

    public static void main(String[] unused) throws Exception {
        byte[] keyBytes = key.getBytes();
        DESedeKeySpec desKeySpec = new DESedeKeySpec(keyBytes);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);

        SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);

        byte[] dataBytes = DATA.getBytes();
        byte[] encBytes = encrypt(dataBytes, secretKey, XFORM);

        System.out.println("Data: " + DATA);
        System.out.println("Encrypted Data: " + new BASE64Encoder().encode(encBytes));
    }
}
