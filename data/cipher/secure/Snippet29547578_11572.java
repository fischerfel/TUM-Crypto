import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.ECParameterSpec;
import java.security.spec.EllipticCurve;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class encryec
{
KeyPairGenerator kpg;
EllipticCurve curve;
ECParameterSpec ecSpec;
KeyPair aKeyPair;
static KeyAgreement aKeyAgree;
KeyPair bKeyPair;
KeyAgreement bKeyAgree;
KeyFactory keyFac;
static String msg;
public static void main(String args[])
{
    Security.addProvider(new BouncyCastleProvider());
    Scanner ss=new Scanner(System.in);

    try{
        String path = "D:\\rp";
        File filePublicKey = new File(path+"\\public.key");
        FileInputStream fis = new FileInputStream(path+"\\public.key");
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();

        // Read Private Key.
        File filePrivateKey = new File(path+"\\private.key");
        fis = new FileInputStream(path+"\\private.key");
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();

        KeyFactory keyFactory = KeyFactory.getInstance("ECDH");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
        encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);

        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        aKeyAgree = KeyAgreement.getInstance("ECDH", "BC");
        aKeyAgree.init(privateKey);                     // exception line
        aKeyAgree.doPhase(publicKey, true);

        byte[] aBys = aKeyAgree.generateSecret(); 
        KeySpec aKeySpec = new DESKeySpec(aBys);
        SecretKeyFactory aFactory = SecretKeyFactory.getInstance("DES");
        Key aSecretKey = aFactory.generateSecret(aKeySpec);
        Cipher aCipher = Cipher.getInstance(aSecretKey.getAlgorithm());   
        aCipher.init(Cipher.ENCRYPT_MODE, aSecretKey);  
        byte[] encText = aCipher.doFinal("Its Rahul".getBytes());

        System.out.println(Base64.encodeBase64String(encText));
        System.out.println(encText);
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}
}
