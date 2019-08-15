import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class DHTest {

    public static void main(String[] args) {
        //Generates keyPairs for Alice and Bob
        KeyPair kp1 = DiffieHellmanModule.genDHKeyPair();
        KeyPair kp2 = DiffieHellmanModule.genDHKeyPair();
        //Gets the public key of Alice(g^X mod p) and Bob (g^Y mod p)
        PublicKey pbk1 = kp1.getPublic();
        PublicKey pbk2 = kp2.getPublic();
        //Gets the private key of Alice X and Bob Y
        PrivateKey prk1 = kp1.getPrivate();
        PrivateKey prk2 = kp2.getPrivate();
        try {
            //Computes secret keys for Alice (g^Y mod p)^X mod p == Bob (g^X mod p)^Y mod p
            SecretKey key1 = DiffieHellmanModule.agreeSecretKey(prk1, pbk2, true);
            SecretKey key2 = DiffieHellmanModule.agreeSecretKey(prk2, pbk1, true);
            //Instantiate the Cipher of algorithm "DES"
            Cipher c = Cipher.getInstance("DES/ECB/PKCS5Padding");
            //Init the cipher with Alice's key1
            c.init(Cipher.ENCRYPT_MODE, key1);
            //Compute the cipher text = E(key,plainText)
            byte[] ciphertext = c.doFinal("Stand and unfold yourself".getBytes());
            //prints ciphertext
            System.out.println("Encrypted: " + new String(ciphertext,"utf-8"));
            //inits the encryptionMode
            c.init(Cipher.DECRYPT_MODE, key2);
            //Decrypts and print
            System.out.println("Decrypted: " + new String(c.doFinal(ciphertext), "utf-8"));
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
