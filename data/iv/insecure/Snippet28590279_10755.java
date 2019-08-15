import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;


public class TripleDes2 
{
    private static final String PLAIN_TEXT = "TESTDATATESTDATATESTDATA";
    private static final String SHARED_KEY = "GD6GTT56HKY4HGF6FH3JG9J5F62FT1";

   public static void main(String args []) throws Exception

{

    String algorithm = "DESede";
    String transformation = "DESede/CBC/PKCS5Padding";

    byte[] keyValue = SHARED_KEY.getBytes("UTF-8");

    DESedeKeySpec keySpec = new DESedeKeySpec(keyValue);

    IvParameterSpec iv = new IvParameterSpec(new byte[8]);

    SecretKey key = SecretKeyFactory.getInstance(algorithm).generateSecret(keySpec);
    Cipher encrypter = Cipher.getInstance(transformation);
    encrypter.init(Cipher.ENCRYPT_MODE, key, iv);

    byte[] input = PLAIN_TEXT.getBytes("UTF-8");

    byte[] encrypted = encrypter.doFinal(input);

    System.out.println(new String(Hex.encodeHex(encrypted)).toUpperCase());
}
}
