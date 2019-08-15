import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptDecryptExample
{
    // "thisIsASecretKey";
    private static byte[] key = { 0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65,
            0x79 };

    public static void main(String[] args) throws Exception
    {
        //********************WORKS**********************
        String x = "Hello";
        System.out.println("Plain Text: " + x);
        String e = EncryptString(x);
        System.out.println("Encrypted: " + e);
        String d = decryptString(e);
        System.out.println("Deccypted: " + d);

        //********************WORKS**********************
        Byte b = 124;
        System.out.println("Plain Byte: "+b.toString());
        String eb = EncryptString(b.toString());
        System.out.println("Encrypted Byte: "+eb);
        String bd = decryptString(eb);
        System.out.println("Decrypted Byte: "+bd);

        //********************DOESNT*WORK*********************
        Byte[] bArray = {23, 42, 55};
        System.out.println("Plain Byte Array: "+bArray[0].toString()+","+bArray[1].toString()+","+bArray[2].toString());
        String eba = EncryptString(bArray.toString());
        System.out.println("Encrypted Byte Array: "+eba.toString());
        String deba = decryptString(eba.toString());
        System.out.println("Decrypted Byte Array: "+deba.getBytes()[0]);  //<--- Doesn't work
        //*********************************************
    }

    public static String EncryptString(String strToEncrypt) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        String encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
        return encryptedString;
    }

    public static String decryptString(String strToDecrypt) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
        return decryptedString;
    }

}
