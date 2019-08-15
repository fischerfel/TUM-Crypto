import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class AESKeGenFromRSA
{
public static void main(String[] args)
{
    try
    {
        // Generate RSA KeyPair for Alice
        Cipher alice_rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // Get RSA KeyPairGenerator Object Instance
        KeyPairGenerator alice_gen = KeyPairGenerator.getInstance("RSA");
        // Generate RSA Assymetric KeyPair
        KeyPair alice_pair = alice_gen.generateKeyPair();
        // Extract Public Key
        PublicKey alice_pub = alice_pair.getPublic();
        // Extract Private Key
        PrivateKey alice_pvt = alice_pair.getPrivate();

        System.out.println();
        System.out.println("Alice Public: " + alice_pub);
        System.out.println();
        System.out.println("Alice Private: " + alice_pvt);

        // Generate RSA KeyPair for Bob
        Cipher bob_rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // Get RSA KeyPairGenerator Object Instance
        KeyPairGenerator bob_gen = KeyPairGenerator.getInstance("RSA");
        // Generate RSA Assymetric KeyPair
        KeyPair bob_pair = bob_gen.generateKeyPair();
        // Extract Public Key
        PublicKey bob_pub = bob_pair.getPublic();
        // Extract Private Key
        PrivateKey bob_pvt = bob_pair.getPrivate();

        System.out.println();
        System.out.println("Bob Public: " + bob_pub);
        System.out.println();
        System.out.println("Bob Private: " + bob_pvt);

        // Create KeyAgreement for Alice
        KeyAgreement alice_agreement =     KeyAgreement.getInstance("DiffieHellman");
        alice_agreement.init(alice_pvt);
        alice_agreement.doPhase(bob_pub, true);
        byte[] alice_secret = alice_agreement.generateSecret();
        SecretKeySpec alice_aes = new SecretKeySpec(alice_secret, "AES");

        // Create KeyAgreement for Bob
        KeyAgreement bob_agreement = KeyAgreement.getInstance("DiffieHellman");
        bob_agreement.init(bob_pvt);
        bob_agreement.doPhase(alice_pub, true);
        byte[] bob_secret = bob_agreement.generateSecret();
        SecretKeySpec bob_aes = new SecretKeySpec(bob_secret, "AES");

        System.out.println();
        System.out.println(alice_aes.equals(bob_aes));
    }
    catch (NoSuchAlgorithmException e)
    {e.printStackTrace();}
    catch (NoSuchPaddingException e)
    {e.printStackTrace();}
    catch (InvalidKeyException e)
    {e.printStackTrace();}
}
}
