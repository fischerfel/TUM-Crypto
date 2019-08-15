import java.security.*;
import java.security.interfaces.*;
import javax.crypto.Cipher;

public class RsaToy {

    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair kp = kpg.generateKeyPair();
        RSAPublicKey pub = (RSAPublicKey) kp.getPublic();
        RSAPrivateKey priv = (RSAPrivateKey) kp.getPrivate();


        Cipher c = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        c.init(Cipher.ENCRYPT_MODE, pub);

        byte [] plain = new byte[100]; // initialize to all zero bytes

        // First encrypt: length of input (plain) is 100

        byte [] cipher = c.doFinal(plain);

        System.out.println("length of cipher is " + cipher.length);

        // Now decrypt: length of input(cipher) is 128;

        c.init(Cipher.DECRYPT_MODE, priv);

        byte [] decrypted_cipher = c.doFinal(cipher);

        System.out.println("length of decrypted cipher is " + decrypted_cipher.length);
    }
}
