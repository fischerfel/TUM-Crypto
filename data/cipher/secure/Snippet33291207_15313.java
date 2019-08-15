package nl.owlstead.stackoverflow;

import static java.nio.charset.StandardCharsets.US_ASCII;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.util.encoders.Hex;

public class ECPublicKeyFactory {

    public static void main(String[] args) throws Exception {

        String name = "secp256r1";

        Security.addProvider(new BouncyCastleProvider());

        // === NOT PART OF THE CODE, JUST GETTING TEST VECTOR ===
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", "BC");
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(name);
        kpg.initialize(ecGenParameterSpec);
        ECPublicKey key = (ECPublicKey) kpg.generateKeyPair().getPublic();
        byte[] x = key.getW().getAffineX().toByteArray();
        byte[] y = key.getW().getAffineY().toByteArray();

        // === here the magic happens ===
        KeyFactory eckf = KeyFactory.getInstance("EC");
        ECPoint point = new ECPoint(new BigInteger(1, x), new BigInteger(1, y));
        ECNamedCurveParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec(name);
        ECParameterSpec spec = new ECNamedCurveSpec(name, parameterSpec.getCurve(), parameterSpec.getG(), parameterSpec.getN(), parameterSpec.getH(), parameterSpec.getSeed());
        ECPublicKey ecPublicKey = (ECPublicKey) eckf.generatePublic(new ECPublicKeySpec(point, spec));
        System.out.println(ecPublicKey.getClass().getName());

        // === test 123 ===
        Cipher ecies = Cipher.getInstance("ECIESwithAES", "BC");
        ecies.init(Cipher.ENCRYPT_MODE, ecPublicKey);
        byte[] ct = ecies.doFinal("owlstead".getBytes(US_ASCII));
        System.out.println(Hex.toHexString(ct));
    }
}
