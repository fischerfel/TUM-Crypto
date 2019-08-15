import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import javax.crypto.BadPaddingException;

public class Encrypt
{
    private static byte[] _key = new byte[] {
            (byte) 0x02, (byte) 0xb2, (byte) 0xc4, (byte) 0x9e,
            (byte) 0xf9, (byte) 0x44, (byte) 0x99, (byte) 0xc9,
            (byte) 0x80, (byte) 0x65, (byte) 0xcd, (byte) 0x8f,
            (byte) 0x69, (byte) 0x2b, (byte) 0x74, (byte) 0x34};

    private static byte[] _iv = new byte[] {
            (byte) 0x69, (byte) 0x2b, (byte) 0x74, (byte) 0x34,
            (byte) 0x02, (byte) 0xb2, (byte) 0xc4, (byte) 0x9e,
            (byte) 0xf9, (byte) 0x44, (byte) 0x99, (byte) 0xc9,
            (byte) 0x80, (byte) 0x65, (byte) 0xcd, (byte) 0x8f};

    public static void main(String[] args)
            throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            BadPaddingException
    {
        if (args.length != 1)
            System.out.println("Error, expecting a single string to encrypt as a command line argument.");
        else
            {
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(_key, "AES"),
                    new IvParameterSpec(_iv));
            byte[] encrypted = cipher.doFinal(args[0].getBytes(StandardCharsets.US_ASCII));
            String result = DatatypeConverter.printBase64Binary(encrypted);
            System.out.println("result: " + result);
            }
    }
}
