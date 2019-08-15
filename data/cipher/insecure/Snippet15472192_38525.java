import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

public class AESExample
{
    public static void main(String[] args)
    {
        try
        {
            String plainData = "my name is laksahan", cipherText, decryptedText;
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();
            cipherText = encrypt(plainData, secretKey);
            System.out.println(cipherText);
            decryptedText = decrypt(cipherText, secretKey);
            System.out.println(decryptedText);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static String encrypt(String plainData, SecretKey secretKey) throws Exception
    {
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] byteDataToEncrypt = plainData.getBytes();
        byte[] byteCipherText = aesCipher.doFinal(byteDataToEncrypt);
        return new BASE64Encoder().encode(byteCipherText);
    }

    public static String decrypt(String cipherData, SecretKey secretKey) throws Exception
    {
        byte[] data = new BASE64Decoder().decodeBuffer(cipherData);
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] plainData = aesCipher.doFinal(data);
        return new String(plainData);
    }

}
