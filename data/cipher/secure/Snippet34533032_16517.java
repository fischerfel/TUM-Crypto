package nl.owlstead.stackoverflow;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PKCS1PublicKey {

    public static RSAPublicKey fromPKCS1Encoding(byte[] pkcs1EncodedPublicKey) {
        // --- parse public key ---
        org.bouncycastle.asn1.pkcs.RSAPublicKey pkcs1PublicKey;
        try {
            pkcs1PublicKey = org.bouncycastle.asn1.pkcs.RSAPublicKey
                    .getInstance(pkcs1EncodedPublicKey);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Could not parse BER PKCS#1 public key structure", e);
        }

        // --- convert to JCE RSAPublicKey
        RSAPublicKeySpec spec = new RSAPublicKeySpec(
                pkcs1PublicKey.getModulus(), pkcs1PublicKey.getPublicExponent());
        KeyFactory rsaKeyFact;
        try {
            rsaKeyFact = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("RSA KeyFactory should be available", e);
        }
        try {
            return (RSAPublicKey) rsaKeyFact.generatePublic(spec);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException(
                    "Invalid RSA public key, modulus and/or exponent invalid", e);
        }
    }

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        String pkey = "MIIBCgKCAQEA2tF2g/muNw9xKTVcIkjUMvMhygtIW49yo1PgbwqDQ/w9MSfEARtYYF6Tenfz0twaR/eI14GXmlIffflORe4eaSuMBhwQFOIKU/1+v1BV3RLqGGblvHTVaMVm49AGiqxNnh1LBbcSrC5UhMqlL/HGiku0oYsbjLzwcLc5ac6aBQVD60wWGNm1g26lRQGRbCLqxVfcWKT3AMvEQK3cEx/En7/5Vg1V8xnJraNMrO8UGnaX8LLJFzYJiSCEShh7F+pMHbf4MaBekw7Aaf5hPJtczNsR137R92Be3OP4idI5NLmTV+Pi1DWlxhjEhswKH88SP+gsW31gS7B/ddECUqewQwIDAQAB";
        Decoder decoder = Base64.getDecoder();
        byte[] dpkey = decoder.decode(pkey);
        RSAPublicKey publicKey = fromPKCS1Encoding(dpkey);

        String plainData = "Hi I m here";
        String data = "aP0nuYYA1hE5odsCkR/DcdRbBvO2Z8IOlqXf/bKZJiG8HELIop90Vno1dKC1qyHEAOXy0gtH7GtJamzoBjDZmHPT6eto9EZP/xE7xZ8L05kjp0z2thLqO7on4C6DrG++TK1j+E3T7V0UeU874WIB0AEVzu1XUKFW6aeuU67a/gdn8N2n7N/WXtlyNSVZXg8f4PeUhGvFJrhINZT7BuMMZj1gZs4wMJPAICwfvVeg02RPH0N3Ybf2iVgRuZlmtQXGTyBlCxe9ybdHzuQM6nXghpLNmaOzCypb+yVs3Da7E0b3/fKQ7JqPSquWex2ERZbIMSTC6oCzc1rOF6iKVAd92Q==";
        byte[] ciphertext = decoder.decode(data);

        // this will fail of course if the "signature" was generated using OAEP - use PSS signatures instead (see comments below)
        verifyBC(publicKey, plainData, ciphertext);
        System.out.flush();
        decryptBC(publicKey, plainData, ciphertext);
        System.out.flush();
        decryptSun(publicKey, plainData, ciphertext);
        System.out.flush();
    }

    private static void decryptBC(RSAPublicKey publicKey, String plainData,
            byte[] ciphertext) throws Exception {
        Cipher oaep = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding", "BC");
        // this *should* fail
        oaep.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] plaintext = oaep.doFinal(ciphertext);
        System.out.println(new String(plaintext, UTF_8));
    }

    private static void decryptSun(RSAPublicKey publicKey, String plainData,
            byte[] ciphertext) throws Exception {
        Cipher oaep = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding", "SunJCE");
        // this fails beautifully
        oaep.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] plaintext = oaep.doFinal(ciphertext);
        System.out.println(new String(plaintext, UTF_8));
    }

    private static void verifyBC(RSAPublicKey publicKey, String plainData,
            byte[] ciphertext) throws Exception {
        // what should work (for PKCS#1 v1.5 signatures), requires Bouncy Castle provider
        Signature sig = Signature.getInstance( "SHA256withRSAandMGF1");
        sig.initVerify(publicKey);
        sig.update(plainData.getBytes(UTF_8));
        System.out.println(sig.verify(ciphertext));
    }
}
