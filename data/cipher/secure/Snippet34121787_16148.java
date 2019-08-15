import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class Receiver {

    public static void main(String[] args) {
        //Sender's out file
        File inFile = new File(args[0]);
        //receiver's keystore
        File keyStoreFile = new File(args[1]);
        //receiver's keystore info
        String password = args[2];
        String alias = args[3];
        String storepass = args[4];
        //sender's cetificate
        File cert = new File(args[5]);

        try {
            //get Sender's out file
            FileInputStream rawDataFromFile = new FileInputStream(inFile);
            byte[] simmetricKey = new byte[256];
            byte[] hash = new byte[256];
            byte[] message;

            rawDataFromFile.read(simmetricKey);
            rawDataFromFile.read(hash);

            int b = rawDataFromFile.available();
            message = new byte[b];

            rawDataFromFile.read(message);
            //decrypt the simmetric key with receiver's private key
            KeyStore myKeyStore = KeyStore.getInstance("JKS");
            FileInputStream inStream = new FileInputStream(keyStoreFile);
            myKeyStore.load(inStream, storepass.toCharArray());
            PrivateKey privatekey = (PrivateKey) myKeyStore.getKey(alias, password.toCharArray());
            //
            Cipher deCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            deCipher.init(Cipher.DECRYPT_MODE, privatekey);

            byte[] key = deCipher.doFinal(simmetricKey);

            System.out.println(Base64.encodeBase64String(key));

        } catch (Exception e) {
            System.out.println("Error del sistema " + e);
            e.printStackTrace();
        }

    }

}
