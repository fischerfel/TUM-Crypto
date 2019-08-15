import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

class Sender{

public static void main(String[] args) {
        //infile.txt
        File inFile = new File(args[0]);
        //outfile.txt
        File outFile = new File(args[1]);
        //mykeystore.jks
        File keyStoreFile = new File(args[2]);
        //mykeystore info
        String alias = args[3];
        String password = args[4];
        String storepass = args[5];
        //receptor certificate
        String receptorCert = args[6];

        try {
            //Read plain text
            FileInputStream rawDataFromFile = new FileInputStream(inFile);
            byte[] plainText = new byte[(int) inFile.length()];
            rawDataFromFile.read(plainText);

            //Create simmetric key
            String key = "Bar12345Bar12345"; // 128 bit key
            String initVector = "RandomInitVector"; // 16 bytes IV
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            //Encrypt plaintext
            byte[] ciphertext = cipher.doFinal(plainText);

            //Hash plaintext
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(plainText);
            byte[] digest = md.digest();

            //Encrypt simmetric key with receiver's public key          
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            PublicKey receptorPublicKey = getPublicKeyFromCert(receptorCert);
            rsaCipher.init(Cipher.ENCRYPT_MODE, receptorPublicKey);
            byte[] simmetricKey = rsaCipher.doFinal(skeySpec.getEncoded());

            //Encrypt hash with my private key
            KeyStore myKeyStore = KeyStore.getInstance("JKS");
            FileInputStream inStream = new FileInputStream(keyStoreFile);
            myKeyStore.load(inStream, storepass.toCharArray());
            PrivateKey privatekey = (PrivateKey) myKeyStore.getKey(alias, password.toCharArray());
            rsaCipher.init(Cipher.ENCRYPT_MODE, privatekey);            
            byte[] encodedHash = rsaCipher.doFinal(digest);

            //Write to outputfile
            FileOutputStream outToFile = new FileOutputStream(outFile);

            outToFile.write(simmetricKey);
            outToFile.write(encodedHash);
            outToFile.write(ciphertext);

            outToFile.close();
            rawDataFromFile.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
    }
    public static PublicKey getPublicKeyFromCert(String certLocation) {
        PublicKey pub = null;
        try {
            InputStream inStream = new FileInputStream(certLocation);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
            inStream.close();
            pub = (PublicKey) cert.getPublicKey();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return pub;
    }
}
