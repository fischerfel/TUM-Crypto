 import org.bouncycastle.openssl.PEMWriter;

 import java.io.StringWriter;
 import java.math.BigInteger;
 import java.security.KeyPair;
 import java.security.KeyPairGenerator;
 import java.security.MessageDigest;
 import java.security.SecureRandom;
 import java.security.Security;
 import java.security.spec.ECFieldFp;
 import java.security.spec.ECParameterSpec;
 import java.security.spec.ECPoint;
 import java.security.spec.EllipticCurve;

 import javax.crypto.KeyAgreement;

 public class X509CertificateGenerator {
     public static void main(String[] args) throws Exception {
         Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

         KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDH", "BC");
         EllipticCurve curve = new EllipticCurve(new ECFieldFp(new BigInteger(
                 "fffffffffffffffffffffffffffffffeffffffffffffffff", 16)), new BigInteger(
                 "fffffffffffffffffffffffffffffffefffffffffffffffc", 16), new BigInteger(
                 "fffffffffffffffffffffffffffffffefffffffffffffffc", 16));

         ECParameterSpec ecSpec = new ECParameterSpec(curve, new ECPoint(new BigInteger(
                 "fffffffffffffffffffffffffffffffefffffffffffffffc", 16), new BigInteger(
                 "fffffffffffffffffffffffffffffffefffffffffffffffc", 16)), new BigInteger(
                 "fffffffffffffffffffffffffffffffefffffffffffffffc", 16), 1);

         keyGen.initialize(ecSpec, new SecureRandom());

         KeyAgreement aKeyAgree = KeyAgreement.getInstance("ECDH", "BC");
         KeyPair aPair = keyGen.generateKeyPair();
         KeyAgreement bKeyAgree = KeyAgreement.getInstance("ECDH", "BC");
         KeyPair bPair = keyGen.generateKeyPair();

         aKeyAgree.init(aPair.getPrivate());
         bKeyAgree.init(bPair.getPrivate());

         aKeyAgree.doPhase(bPair.getPublic(), true);
         bKeyAgree.doPhase(aPair.getPublic(), true);

         MessageDigest hash = MessageDigest.getInstance("SHA1", "BC");

         System.out.println(new String(hash.digest(aKeyAgree.generateSecret())));
         System.out.println(new String(hash.digest(bKeyAgree.generateSecret())));
         System.out.println(aPair.getPrivate());

         StringWriter pemWrtPublic = new StringWriter();
         PEMWriter pubkey = new PEMWriter(pemWrtPublic);
         pubkey.writeObject(aPair.getPublic());
         pubkey.flush();
         String pemPublicKey = pemWrtPublic.toString();
         System.out.println(pemPublicKey);

         StringWriter pemWrtPrivate = new StringWriter();
         PEMWriter privkey = new PEMWriter(pemWrtPrivate);
         privkey.writeObject(aPair.getPrivate());
         privkey.flush();
         String pemPrivateKey = pemWrtPrivate.toString();
         System.out.println(pemPrivateKey);
     }
 }     
