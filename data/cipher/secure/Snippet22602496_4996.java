import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.logging.Level;    
import java.util.logging.Logger;    
import javax.crypto.*;
import java.util.*;    
import javax.crypto.spec.DHParameterSpec;

public class Encrypting{

    public static void main(String[] args) {
    try {
        KeyPair alice_key;

      KeyPair bob_key ;

        String plaintext="hi how r u";

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");

        AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator
            .getInstance("DH");

        paramGen.init(1024);

        AlgorithmParameters params = paramGen.generateParameters();

        DHParameterSpec dhSpec = (DHParameterSpec) params
            .getParameterSpec(DHParameterSpec.class);

        keyGen.initialize(dhSpec);

         alice_key = keyGen.generateKeyPair();

         bob_key = keyGen.generateKeyPair();

        Cipher cipher = Cipher.getInstance("RSA");

        SecretKey secret_alice = combine(alice_key.getPrivate(), bob_key.getPublic());

         cipher.init(Cipher.ENCRYPT_MODE, secret_alice);

        byte[] x = cipher.doFinal(plaintext.getBytes());

        System.out.println("encrypted message");

        for(int i=0;i<x.length;i++)
            System.out.print((char)x[i]);

        cipher.init(Cipher.DECRYPT_MODE, secret_alice);

        byte[] y = cipher.doFinal(x);

        System.out.println();

        System.out.println("decrypted message");

         for(int i=0;i<y.length;i++)

            System.out.print((char)y[i]);

    SecretKey secret_bob = combine(bob_key.getPrivate(),

            alice_key.getPublic());

    System.out.println(Arrays.toString(secret_alice.getEncoded()));

        System.out.println(Arrays.toString(secret_bob.getEncoded())); 

    } catch (NoSuchAlgorithmException ex) {

        Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);

    } catch (InvalidParameterSpecException ex) {

        Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);

    } catch (InvalidAlgorithmParameterException ex) {

        Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);

    } catch (NoSuchPaddingException ex) {

        Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);

    } catch (InvalidKeyException ex) {

        Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);

    } catch (IllegalBlockSizeException ex) {

        Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);

    } catch (BadPaddingException ex) {

        Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);

    }
}

private static SecretKey combine(PrivateKey private1, PublicKey public1)  {

     SecretKey secretKey=null;

    try {

        KeyAgreement ka = KeyAgreement.getInstance("DH");

        ka.init(private1);

        ka.doPhase(public1, true);

         secretKey = ka.generateSecret("DES");

        return secretKey;

    } catch (NoSuchAlgorithmException ex) {

        Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);

    } catch (InvalidKeyException ex) {

        Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);

    }

    return secretKey;

}

}
