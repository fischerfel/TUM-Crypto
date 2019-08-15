package nl.maartenbodewes.stackoverflow;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

public class GenerateAndWrapHMACKey {

    public static SecretKey generateHMACKey() throws Exception {
        final KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("HMAC KeyGeneration should be available");
        }
        keyGen.init(128);
        SecretKey key = keyGen.generateKey();
        return key;
    }

    public static void writeToFile(SecretKey key, String filename)
            throws IOException {
        // file handling probably should be in a separate class
        Files.write((new File(filename)).toPath(), key.getEncoded());
    }

    public static RSAPublicKey readRSAPublicKey(String filename) throws IOException, InvalidKeySpecException {
        try (PemReader reader = new PemReader(new FileReader(filename))) {
            PemObject pemObject = reader.readPemObject();
            KeyFactory kf;
            try {
                kf = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("RSA key factory not available", e);
            }
            KeySpec keySpec = new X509EncodedKeySpec(pemObject.getContent());
            try {
                return (RSAPublicKey) kf.generatePublic(keySpec);
            } catch (ClassCastException e) {
                throw new InvalidKeySpecException("That's no RSA key", e);
            }
        }
    }

    public static byte[] wrapKey(Key key, RSAPublicKey wrappingKey) throws InvalidKeyException, IllegalBlockSizeException {
        Cipher rsaWrapper;
        try {
            rsaWrapper = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
            rsaWrapper.init(Cipher.WRAP_MODE, wrappingKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException("RSA OAEP should be available for RSA public key", e);
        }
        return rsaWrapper.wrap(key);
    }

    public static void main(String[] args) throws Exception {
        // we need an RSA PEM key first I guess :)
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024, new SecureRandom());
        KeyPair kp = kpg.generateKeyPair();
        String publicKeyFilename = "rsa_pub.pem";
        try (PemWriter pemWriter = new PemWriter(new FileWriter(publicKeyFilename))) {
            pemWriter.writeObject(new PemObject("PUBLIC KEY", kp.getPublic().getEncoded()));
        }

        RSAPublicKey wrappingRSAPublicKey = readRSAPublicKey(publicKeyFilename);
        SecretKey hmacKey = generateHMACKey();
        byte[] wrappedKey = wrapKey(hmacKey, wrappingRSAPublicKey);
        System.out.println(Base64.getEncoder().encodeToString(wrappedKey));
    }
}
