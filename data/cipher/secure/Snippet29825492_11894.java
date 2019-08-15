/*
PrivatePublicKey.java   
*/
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import javax.crypto.Cipher;
/* for IBM JDK need to replace: */
//import java.util.Base64;
/* with: */
import org.apache.commons.codec.binary.Base64;

public class PrivatePublicKey 
{
    public static void main(String[] args) throws Exception 
    {
        try 
        {
            PublicKeyReader myPublic = new PublicKeyReader();
            PublicKey publicKey = myPublic.get("./rsakpubcert.key");

            PrivateKeyReader myPrivate = new PrivateKeyReader();
            PrivateKey privateKey = myPrivate.get("./rsakprivnopassword.key");

            // Let's encrypt with private and decrypt with public
            // Encrypt with private key
            String firstString = "Ishana";

            Cipher privateEncryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            privateEncryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);

            byte[] encryptedFirstString = privateEncryptCipher.doFinal(firstString.getBytes());
            String encodedEncryptedFirstString = Base64.encodeBase64String(encryptedFirstString);

            System.out.println("Encoded encrypted String for Ishana: " + encodedEncryptedFirstString);

            // Decrypt with public key
            // First decode the string
            byte[] decodedEncryptedFirstString = Base64.decodeBase64(encodedEncryptedFirstString);

            Cipher publicDecryptCipher = Cipher
                .getInstance("RSA/ECB/PKCS1Padding");
            publicDecryptCipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] decryptedFirstStringByte =     publicDecryptCipher.doFinal(decodedEncryptedFirstString);
            System.out.println("Decrypted String for Ishana: " + new String(decryptedFirstStringByte));
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}
