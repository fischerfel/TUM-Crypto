import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

public class AESExample
{

    public static void main(String[] args)
    {
        try
        {
            byte[]key={-4, -14, 106, -75, -9, 65, -95, 77, -52, 73, -87, -101, 80, 94, -59, -66};
            String plainData = "my name is laksahan", cipherText, decryptedText;
            System.out.println(key.length);
            cipherText = encrypt(plainData, key);
            System.out.println(cipherText);
            decryptedText = decrypt(cipherText, key);
            System.out.println(decryptedText);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String encrypt(String plainData, byte[] key) throws Exception
    {
        Cipher aesCipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] byteDataToEncrypt = plainData.getBytes();
        byte[] byteCipherText = aesCipher.doFinal(byteDataToEncrypt);
        return new BASE64Encoder().encode(byteCipherText);
    }

    public static String decrypt(String cipherData, byte[] key) throws Exception
    {
        byte[] data = new BASE64Decoder().decodeBuffer(cipherData);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] plainData = aesCipher.doFinal(data);
        return new String(plainData);
    }

}
