package test;

import java.io.StringWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.generators.KDF2BytesGenerator;
import org.bouncycastle.crypto.kems.ECIESKeyEncapsulation;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemObject;

public class TestECKey {

    public static void main(String[] args) throws Throwable {
        // add instance of provider class
        Security.addProvider(new BouncyCastleProvider());

        // Get domain parameters for example curve
        String name = "prime256v1"; // AndroidPay used
        ECNamedCurveParameterSpec ecp = ECNamedCurveTable.getParameterSpec(name);   // when use prime256v1

        // Buffer for ECIESKeyEncapsulation output
        byte[]                    kemOut = new byte[65];    // when use prime256v1 or secp256r1

        ECDomainParameters domainParams = new ECDomainParameters(ecp.getCurve(),
                                                                 ecp.getG(), ecp.getN(), ecp.getH(),
                                                                 ecp.getSeed());

        // Generate a private key and a public key
        System.out.println("name: " + name);
        AsymmetricCipherKeyPair keyPair;
        ECKeyGenerationParameters keyGenParams = new ECKeyGenerationParameters(domainParams, new SecureRandom());
        ECKeyPairGenerator generator = new ECKeyPairGenerator();
        generator.init(keyGenParams);
        keyPair = generator.generateKeyPair();

        ECPublicKeyParameters publicKey = (ECPublicKeyParameters) keyPair.getPublic();

        System.out.println("====KEM====");

        // Set ECIES-KEM parameters
        SecureRandom              rnd = new SecureRandom();
        ECIESKeyEncapsulation     kem;

        // Test basic ECIES-KEM
        KDF2BytesGenerator        kdf = new KDF2BytesGenerator(new SHA256Digest());

        kem = new ECIESKeyEncapsulation(kdf, rnd);

        kem.init(publicKey);
        kem.encrypt(kemOut, 128);

        System.out.println("AndroidPay's publicKey:");
        System.out.println("out(Base64)[" + new String(Base64.encode(kemOut)) + "]");

        System.out.println("====PKCS#8 (for private key)====");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
        kpg.initialize(ecp);

        // Key pair to store public and private key
        KeyPair keyPair4Cipher = kpg.generateKeyPair();

        JcaPKCS8Generator gen2 = new JcaPKCS8Generator(keyPair4Cipher.getPrivate(), null);
        PemObject obj2 = gen2.generate();
        StringWriter sw2 = new StringWriter();
        try (JcaPEMWriter pw = new JcaPEMWriter(sw2)) {
            pw.writeObject(obj2);
        }
        String pkcs8Key2 = sw2.toString();
        System.out.println(pkcs8Key2);

        System.out.println("====CIPHER (encryption)====");
        Cipher iesCipher = Cipher.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);
        iesCipher.init(Cipher.ENCRYPT_MODE, keyPair4Cipher.getPublic());

        byte[] plain = "plaintext".getBytes();

        byte[] cipher = iesCipher.doFinal(plain);

        System.out.println("cipher:" + new String(Base64.encode(cipher)));



        System.out.println("====CIPHER (decryption)====");
        Cipher iesCipher2 = Cipher.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);
        iesCipher2.init(Cipher.DECRYPT_MODE, keyPair4Cipher.getPrivate());

        byte[] plain2 = iesCipher2.doFinal(cipher);

        System.out.println("plain2:" + new String(plain2));
    }

}
