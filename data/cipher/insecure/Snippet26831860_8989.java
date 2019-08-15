import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Diffie-Hellman module for demonstrating KeyAgreement Algorithm
 */
public class DiffieHellmanModule {

    private static final int AES_KEY_SIZE = 128;

    public static void main(String[] args) {
        // Generates keyPairs for Alice and Bob
        KeyPair kp1 = DiffieHellmanModule.genDHKeyPair();
        KeyPair kp2 = DiffieHellmanModule.genDHKeyPair();
        // Gets the public key of Alice(g^X mod p) and Bob (g^Y mod p)
        PublicKey pbk1 = kp1.getPublic();
        PublicKey pbk2 = kp2.getPublic();
        // Gets the private key of Alice X and Bob Y
        PrivateKey prk1 = kp1.getPrivate();
        PrivateKey prk2 = kp2.getPrivate();
        try {
            // Computes secret keys for Alice (g^Y mod p)^X mod p == Bob (g^X
            // mod p)^Y mod p
            SecretKey key1 = DiffieHellmanModule.agreeSecretKey(prk1, pbk2,
                    true);
            SecretKey key2 = DiffieHellmanModule.agreeSecretKey(prk2, pbk1,
                    true);
            // Instantiate the Cipher of algorithm "DES"
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            // Init the cipher with Alice's key1
            c.init(Cipher.ENCRYPT_MODE, key1);
            // Compute the cipher text = E(key,plainText)
            byte[] ciphertext = c.doFinal("Stand and unfold yourself"
                    .getBytes());
            // prints ciphertext
            System.out.println("Encrypted: " + new String(ciphertext, "utf-8"));
            // inits the encryptionMode
            c.init(Cipher.DECRYPT_MODE, key2);
            // Decrypts and print
            System.out.println("Decrypted: "
                    + new String(c.doFinal(ciphertext), "utf-8"));
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static KeyPairGenerator kpg;

    static {
        try {
            // === Generates and inits a KeyPairGenerator ===

            // changed this to use default parameters, generating your
            // own takes a lot of time and should be avoided
            // use ECDH or a newer Java (8) to support key generation with
            // higher strength
            kpg = KeyPairGenerator.getInstance("DH");
            kpg.initialize(1024);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static SecretKey agreeSecretKey(PrivateKey prk_self,
            PublicKey pbk_peer, boolean lastPhase) throws Exception {
        // instantiates and inits a KeyAgreement
        KeyAgreement ka = KeyAgreement.getInstance("DH");
        ka.init(prk_self);
        // Computes the KeyAgreement
        ka.doPhase(pbk_peer, lastPhase);
        // Generates the shared secret
        byte[] secret = ka.generateSecret();

        // === Generates an AES key ===

        // you should really use a Key Derivation Function instead, but this is
        // rather safe

        MessageDigest sha256 = MessageDigest.getInstance("SHA-256"); 
        byte[] bkey = Arrays.copyOf(
                sha256.digest(secret), AES_KEY_SIZE / Byte.SIZE);

        SecretKey desSpec = new SecretKeySpec(bkey, "AES");
        return desSpec;
    }

    public static KeyPair genDHKeyPair() {
        return kpg.genKeyPair();
    }
}
