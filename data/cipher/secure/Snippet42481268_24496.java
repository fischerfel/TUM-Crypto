import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class cryptoAndBigIntegerFIX {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException{
        Security.addProvider(new BouncyCastleProvider());
        System.out.println("input <> encrypted <> decrypted");
        cryptoAndBigIntegerFIX.keygen();

        BigInteger encryptbytes;
        BigInteger decryptbytes;

        //Multiple tests with powers of 3 for some reason :D
        for(int i=1;i<1000;i*=3){
            encryptbytes = cryptoAndBigIntegerFIX.encrypt(new BigInteger(""+i));
            System.out.print(i + " <> " + encryptbytes.intValue() + " <> ");
            decryptbytes = cryptoAndBigIntegerFIX.decrypt(encryptbytes);
            System.out.println(decryptbytes.intValue());            
        }
    }

    public static RSAPrivateKey priv;
    public static RSAPublicKey pub;

    public static void keygen() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException{
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

        generator.initialize(512);
        KeyPair keyPair = generator.generateKeyPair();
        priv = (RSAPrivateKey) keyPair.getPrivate();
        pub = (RSAPublicKey) keyPair.getPublic();       
    }

    //Encrypt with javas API
    public static BigInteger encrypt(BigInteger bg) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException{
        byte[] encoded;
        Cipher cipher=Cipher.getInstance("RSA/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, pub);
        encoded=cipher.doFinal(bg.toByteArray());
        return new BigInteger(encoded);
    }

    //Decrypt manually
    public static BigInteger decrypt(BigInteger bg){
        BigInteger decoded = bg.modPow(priv.getPrivateExponent(),priv.getModulus());
        return decoded;

    }

}
