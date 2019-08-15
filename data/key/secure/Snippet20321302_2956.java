import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class doThis {

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        String strDataToEncrypt = "Testing Encryption";
        byte[] byteDataToTransmit = strDataToEncrypt.getBytes();
        //41 6E 6B 61 72 61 6F 20 49 74 74 61 64 69
        //byte[] byteDataToTransmit = new byte []
        {
            0x41,0x6E,0x6B,0x61,0x72,0x61,0x6F,0x20,0x49,0x74,0x74,0x61,0x64,0x69
        };
        try {

            byte [] keyBytes= new byte [] {0x21,0x0a,0x03,0x23,0x45,0x29,0x78,0x12,0x35,
                                           0x45,0x67,0x78,0x21,0x13,0x34,

                                           0x56,0x67,0x45,0x12,0x9,0x38,0x0e,0x20,
                                           0x15,0x21,0x0a,0x03,0x23,0x45,0x0b,0x15,0x0c
                                          };

            byte[] encrypted= aesEncrypt(byteDataToTransmit,keyBytes);

            System.out.println("\n AES Encrypted Data is  "+new String (encrypted));

            byte [] byteDecrypt=aesDecrypt(bytestrEncrypt, keyBytes);
            System.out.println("\n AES Decrypted Data is"+byteDecrypt);
            // byte [] byteDecrypt=aesDecrypt(encrypted , keyBytes);

            //System.out.println("\n AES Decrypted Data is"+new String(byteDecrypt));
        }
        catch(Exception exp)
        {
            System.out.println(" Exception caught " + exp);
            exp.printStackTrace();
        }
    }

    public static byte[] aesEncrypt(byte[] original, byte[] key)
    {
        try
        {
            SecretKeySpec keySpec = null;
            Cipher cipher = null;
            {
                keySpec = new SecretKeySpec(key, "AES/ECB/PKCS7Padding");
                cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
                cipher.init(Cipher.ENCRYPT_MODE, keySpec); // encryption
            }
            return cipher.doFinal(original);
        }
        catch(Exception e)
        {
            //  Logger.e(e.toString());
        }
        return null;
    }

    public static byte[] aesDecrypt(byte[] encrypted, byte[] key)
    {
        try
        {
            SecretKeySpec keySpec = null;
            Cipher cipher = null;

            {
                keySpec = new SecretKeySpec(key, "AES/ECB/PKCS7Padding");

                cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
                cipher.init(Cipher.DECRYPT_MODE, keySpec);
            }

            System.out.println("In Decryprion \n"+ new String (encrypted));
            return cipher.doFinal(encrypted);

        }
        catch(Exception e)
        {
            //  Logger.e(e.toString());
        }
        return null;
    }
}
