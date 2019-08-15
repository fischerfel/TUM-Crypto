import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CipherBasicSample
{
    public static void main(String args[]) throws Exception
    {
        Security.addProvider(new BouncyCastleProvider());

        // text to cipher
        String secret = "secret";

        // create the key to cipher an decipher
        KeyGenerator kg = KeyGenerator.getInstance("AES","BC");
        kg.init(128);
        SecretKey sk = kg.generateKey();
        Key key = new SecretKeySpec(sk.getEncoded(), "AES");

        // get a cipher instance
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
        // init to encrypt mode
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // encrypt the text
        cipher.update(secret.getBytes());
        byte[] secretEncrypt = cipher.doFinal();

        System.out.println("Encrypt text: " + new String(secretEncrypt));

        // get a cipher instance
        Cipher decipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
        // init to decrypt mode
        decipher.init(Cipher.DECRYPT_MODE, key);
        // decrypt the text
        decipher.update(secretEncrypt);
        byte[] secretDecrypt = decipher.doFinal();

        System.out.println("Encrypt text: " + new String(secretDecrypt));
    }
}
