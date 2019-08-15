import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class Encrypter
{
    public static void main(String[] args)
    {
        try {   
            String data = "111111111222";
            String privateKeyString = "here is my privat key";

            byte [] encoded = Base64.getDecoder().decode(privateKeyString);
            System.out.println("encoded = "+encoded);

            java.security.Security.addProvider( new org.bouncycastle.jce.provider.BouncyCastleProvider());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            KeySpec ks = new PKCS8EncodedKeySpec(encoded);
            RSAPrivateKey privKey = (RSAPrivateKey) keyFactory.generatePrivate(ks);
            System.out.println("privKey = "+privKey);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, privKey);

            byte[] cipherData = cipher.doFinal(data.getBytes());

            String card = Base64.getEncoder().encodeToString(cipherData);
            System.out.println("data = "+card);
        }catch (Exception e){
            e.printStackTrace(System.out);
        }

    }
}
