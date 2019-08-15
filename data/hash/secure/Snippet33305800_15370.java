package mysha.mysha;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;

import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class MySHA256 { 
    public static void main(String[] args) throws Exception {
        //compute SHA256 first
        Security.addProvider(new BouncyCastleProvider());
        String s = "1234";
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(s.getBytes());
        byte[] outputDigest = messageDigest.digest();

        AlgorithmIdentifier sha256Aid = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256, null);
        DigestInfo di = new DigestInfo(sha256Aid, outputDigest);
        //sign SHA256 with RSA
        PrivateKey privateKey = Share.loadPk8("D:/key.pk8");
        Signature rsaSignature = Signature.getInstance("RSA");
        rsaSignature.initSign(privateKey);
        rsaSignature.update(di.toASN1Primitive().getEncoded());
        byte[] signed = rsaSignature.sign();
        System.out.println("method 1: "+bytesToHex(signed));


        //compute SHA256withRSA as a single step
        Signature rsaSha256Signature = Signature.getInstance("SHA256withRSA");
        rsaSha256Signature.initSign(privateKey);
        rsaSha256Signature.update(s.getBytes());
        byte[] signed2 = rsaSha256Signature.sign();
        System.out.println("method 2: "+bytesToHex(signed2));
    }
    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
