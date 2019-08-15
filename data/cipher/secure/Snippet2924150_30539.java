import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

import org.apache.commons.lang.ArrayUtils;

public class TEST {

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
 KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
 keyPairGenerator.initialize(1024);
 return keyPairGenerator.generateKeyPair();
    }

    public static void main(String[] args) throws Exception {

 KeyPair keyPair = generateKeyPair();
 RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

 System.out.println("Priv modulus len = " + privateKey.getModulus().bitLength());
 System.out.println("Priv exponent len = " + privateKey.getPrivateExponent().bitLength());
 System.out.println("Priv modulus toByteArray len = " + privateKey.getModulus().toByteArray().length);

 byte[] byteArray = privateKey.getModulus().toByteArray();
 // the byte at index 0 have no value (in every generation it is always zero)
 byteArray = ArrayUtils.subarray(byteArray, 1, byteArray.length);

 System.out.println("byteArray size: " + byteArray.length);

 RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
 Cipher cipher = Cipher.getInstance("RSA", "BC");
 cipher.init(Cipher.ENCRYPT_MODE, publicKey);
 byte[] encryptedBytes = cipher.doFinal(byteArray);

 System.out.println("Success!");
    }

}
