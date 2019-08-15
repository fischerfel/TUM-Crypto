package tests.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.Iterator;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyConverter;

import Decoder.BASE64Encoder;

public class GPGTest {

    char[] pass = { 'p', 'a', 's', 's' };

    public static void main(String[] args) throws Exception {

        GPGTest gTest = new GPGTest();
        gTest.setUpCipher();
    }

    public void setUpCipher() throws NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException,
            NoSuchProviderException, BadPaddingException,
            NoSuchPaddingException {

        System.out.println("--- setup cipher ---");

        // public key
        String publicKeyFilePath = "E:/Programs/Keys/GPG/Public/public.key";
        File publicKeyFile = new File(publicKeyFilePath);

        // secret key
        String secretKeyFilePath = "E:/Programs/Keys/GPG/Secret/private.key";
        File secretKeyFile = new File(secretKeyFilePath);

        // security provider
        Security.addProvider(new BouncyCastleProvider());

        try {

            // Read the public key
            FileInputStream pubIn = new FileInputStream(publicKeyFile);
            PGPPublicKey pgpPubKey = readPublicKey(pubIn);
            PublicKey pubKey = new JcaPGPKeyConverter().getPublicKey(pgpPubKey);

            // Read the private key
            FileInputStream secretIn = new FileInputStream(secretKeyFile);
            PGPSecretKey pgpSecretKey = readSecretKey(secretIn);
            PGPPrivateKey pgpPrivKey = pgpSecretKey
                    .extractPrivateKey(new BcPBESecretKeyDecryptorBuilder(
                            new BcPGPDigestCalculatorProvider()).build(pass));
            PrivateKey privKey = new JcaPGPKeyConverter()
                    .getPrivateKey(pgpPrivKey);

            Cipher cipher = Cipher.getInstance("RSA");

            // Encrypt data
            byte[] encData = encryptData(cipher, pubKey, new String(
                    "#data1\n#data2\n#data3\n").getBytes());
            String cryptString = new BASE64Encoder().encode(encData);

            System.out.println("\n\nEncrypted data=" + cryptString);

            // Decrypt data
            byte[] decData = decryptData(cipher, privKey, encData);
            String decryptString = new BASE64Encoder().encode(decData);

            System.out.println("\n\nDecrypted data=" + decryptString);

        } catch (Exception e) {
            System.out.println("Setup cipher exception");
            System.out.println(e.toString());
        }
    }

    public byte[] encryptData(Cipher cipher, PublicKey pubKey, byte[] clearText) {

        byte[] encryptedBytes = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            encryptedBytes = cipher.doFinal(clearText);

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return encryptedBytes;
    }

    public byte[] decryptData(Cipher cipher, PrivateKey privKey, byte[] encryptedText) {

        byte[] decryptedBytes = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            decryptedBytes = cipher.doFinal(encryptedText);

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return decryptedBytes;
    }

    private static PGPPublicKey readPublicKey(InputStream input) throws IOException, PGPException {

        PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(input));

        Iterator<?> keyRingIter = pgpPub.getKeyRings();
        while (keyRingIter.hasNext()) {
            PGPPublicKeyRing keyRing = (PGPPublicKeyRing) keyRingIter.next();

            Iterator<?> keyIter = keyRing.getPublicKeys();
            while (keyIter.hasNext()) {
                PGPPublicKey key = (PGPPublicKey) keyIter.next();

                if (key.isEncryptionKey()) {
                    return key;
                }
            }
        }

        throw new IllegalArgumentException(
                "Can't find encryption key in key ring.");
    }

    private static PGPSecretKey readSecretKey(InputStream input) throws IOException, PGPException {

        PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(input));

        Iterator<?> keyRingIter = pgpSec.getKeyRings();
        while (keyRingIter.hasNext()) {
            PGPSecretKeyRing keyRing = (PGPSecretKeyRing) keyRingIter.next();

            Iterator<?> keyIter = keyRing.getSecretKeys();
            while (keyIter.hasNext()) {
                PGPSecretKey key = (PGPSecretKey) keyIter.next();

                if (key.isSigningKey()) {
                    return key;
                }
            }
        }

        throw new IllegalArgumentException(
                "Can't find signing key in key ring.");
    }

}
