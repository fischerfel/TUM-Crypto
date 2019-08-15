import java.io.PrintStream;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class test
{

    public static void main(String[] args)
    throws Exception
    {
        //byte[] encryptionKey = "Es6XYPkgCV75J95Y".getBytes(StandardCharsets.UTF_8);
        byte[] encryptionKey = "Es6XYPkgCV75J95Y".getBytes(StandardCharsets.ISO_8859_1);
        //byte[] plainText = args[0].getBytes(StandardCharsets.UTF_8);
        byte[] plainText = args[0].getBytes(StandardCharsets.ISO_8859_1);
        MyCrypto aes = new MyCrypto(encryptionKey);
        byte[] cipherText = aes.encrypt(plainText);
        byte[] decryptedCipherText = aes.decrypt(cipherText);

        System.out.println(new String(plainText));
        System.out.println(new String(cipherText));
        System.out.println(new String(decryptedCipherText));
    }

}

class MyCrypto
{
    private byte[] key;

    private static final String ALGORITHM = "AES";

    public MyCrypto(byte[] key)
    {
        this.key = key;
    }

    /**
     * Encrypts the given plain text
     *
     * @param plainText The plain text to encrypt
     */
    public byte[] encrypt(byte[] plainText) throws Exception
    {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(plainText);
    }

    /**
     * Decrypts the given byte array
     *
     * @param cipherText The data to decrypt
     */
    public byte[] decrypt(byte[] cipherText) throws Exception
    {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        return cipher.doFinal(cipherText);
    }
}
