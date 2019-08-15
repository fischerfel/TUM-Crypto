import static org.bouncycastle.util.encoders.Hex.toHexString;

import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

public class CompareCertAndKey {

    /**
     * Checks if the certificate and RSA private key match.
     * 
     * @param args the path to the certificate file in args[0] and that of the private key in args[1]
     */
    public static void main(String[] args) {
        try {
            final PemReader certReader = new PemReader(new FileReader(args[0]));
            final PemObject certAsPemObject = certReader.readPemObject();
            if (!certAsPemObject.getType().equalsIgnoreCase("CERTIFICATE")) {
                throw new IllegalArgumentException("Certificate file does not contain a certificate but a " + certAsPemObject.getType());
            }
            final byte[] x509Data = certAsPemObject.getContent();
            final CertificateFactory fact = CertificateFactory.getInstance("X509");
            final Certificate cert = fact.generateCertificate(new ByteArrayInputStream(x509Data));
            if (!(cert instanceof X509Certificate)) {
                throw new IllegalArgumentException("Certificate file does not contain an X509 certificate");
            }

            final PublicKey publicKey = cert.getPublicKey();
            if (!(publicKey instanceof RSAPublicKey)) {
                throw new IllegalArgumentException("Certificate file does not contain an RSA public key but a " + publicKey.getClass().getName());
            }

            final RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
            final byte[] certModulusData = rsaPublicKey.getModulus().toByteArray();

            final MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            final byte[] certID = sha1.digest(certModulusData);
            final String certIDinHex = toHexString(certID);


            final PemReader keyReader = new PemReader(new FileReader(args[1]));
            final PemObject keyAsPemObject = keyReader.readPemObject();
            if (!keyAsPemObject.getType().equalsIgnoreCase("PRIVATE KEY")) {
                throw new IllegalArgumentException("Key file does not contain a private key but a " + keyAsPemObject.getType());
            }

            final byte[] privateKeyData = keyAsPemObject.getContent();
            final KeyFactory keyFact = KeyFactory.getInstance("RSA");
            final KeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyData);
            final PrivateKey privateKey = keyFact.generatePrivate(keySpec);
            if (!(privateKey instanceof RSAPrivateKey)) {
                throw new IllegalArgumentException("Key file does not contain an X509 encoded private key");
            }
            final RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
            final byte[] keyModulusData = rsaPrivateKey.getModulus().toByteArray();
            final byte[] keyID = sha1.digest(keyModulusData);
            final String keyIDinHex = toHexString(keyID);

            System.out.println(args[0] + " : " + certIDinHex);
            System.out.println(args[1] + " : " + keyIDinHex);
            if (certIDinHex.equalsIgnoreCase(keyIDinHex)) {
                System.out.println("Match");
                System.exit(0);
            } else {
                System.out.println("No match");
                System.exit(-1);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(-2);
        }
    }
}
