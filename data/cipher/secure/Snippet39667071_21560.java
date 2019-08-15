import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by Suchit Pandya on 9/16/2016.
 */
public class EncryptLogic2 {

    private String publicKeyStoreFileName = "C:\\Program Files\\Java\\jdk1.8.0_51\\jre\\lib\\security\\cacerts";
    private String pubKeyStorePwd = "password";
    private String pubKeyAlias="certName";
    private static final String JKS = "JKS";
    private static final String SHA1PRNG = "SHA1PRNG";
    private static final String RSA = "RSA/ECB/PKCS1Padding";
    private static final int CONST_16 = 16;
    private static byte[] asKey;


    public String TestMethod(final String clearText) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, KeyStoreException,
            CertificateException, IOException, UnrecoverableKeyException {

        String ecryptedData = generateEncryptedData(clearText);
        String decryptedData = generateDecryptedData(ecryptedData);
        System.out.println("*********** Decrypted Data = " + decryptedData);
        return decryptedData;
    }

    private KeyStore loadKeyStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
            IOException {
        KeyStore keystore = KeyStore.getInstance(JKS);
        FileInputStream tmp = new FileInputStream(publicKeyStoreFileName);
        keystore.load(tmp, pubKeyStorePwd.toCharArray());
        tmp.close();
        return keystore;
    }

    private String generateEncryptedData(final String data) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
            KeyStoreException, CertificateException, IOException, UnrecoverableKeyException {
        Base64 base64 = new Base64();
        X509Certificate cert;
        KeyStore keystore = loadKeyStore();
        cert = (java.security.cert.X509Certificate) keystore.getCertificate(pubKeyAlias);
        Cipher rsa = Cipher.getInstance(RSA);
        Key key = cert.getPublicKey();
        rsa.init(Cipher.ENCRYPT_MODE, key);

        byte[] ecrypteddata = rsa.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(ecrypteddata);
    }

    public String generateDecryptedData(final String encryptedData) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
            KeyStoreException, CertificateException, IOException, UnrecoverableKeyException {
        Base64 base64 = new Base64();
        X509Certificate cert;
        KeyStore keystore = loadKeyStore();
        Cipher rsa = Cipher.getInstance(RSA);
        Key key = keystore.getKey(pubKeyAlias, pubKeyStorePwd.toCharArray());
        rsa.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = base64.decode(encryptedData);
        byte[] decodedData = (rsa.doFinal(decodedValue));
        return new String(decodedData, StandardCharsets.UTF_8);
    }
}
