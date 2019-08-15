package XXXX;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


public class EncryptDecryptUtil {
    private String publicKeyStoreFileName = "C:\\Program Files\\Java\\jdk1.8.0_51\\jre\\lib\\security\\cacerts";
    private String pubKeyStorePwd = "XXX";
    private String pubKeyAlias="XXXX";
    private static final String JKS = "JKS";
    private static final int CONST_16 = 16;    

    public String TestMethod(final String clearText) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, KeyStoreException,
            CertificateException, IOException, UnrecoverableKeyException {    
        byte[] ecryptedAESKey = generateEncryptedData("TEST");
        System.out.println("Encrypted Key = " + ecryptedAESKey);
        System.out.println("Decrypted Key = " + generateDecryptedData(ecryptedAESKey));
        return generateDecryptedData(ecryptedAESKey);
    }

    private KeyStore loadKeyStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
            IOException {
        KeyStore keystore = KeyStore.getInstance(JKS);
        FileInputStream tmp = new FileInputStream(publicKeyStoreFileName);
        keystore.load(tmp, pubKeyStorePwd.toCharArray());
        tmp.close();
        return keystore;
    } 

    private byte[] generateEncryptedData(final String data) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
            KeyStoreException, CertificateException, IOException, UnrecoverableKeyException {
            Base64 base64 = new Base64();
            X509Certificate cert;
            KeyStore keystore = loadKeyStore();
            cert = (java.security.cert.X509Certificate) keystore.getCertificate(pubKeyAlias);
            Cipher rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            rsa.init(Cipher.ENCRYPT_MODE, cert);

            byte[] ecrypteddata = (base64.encode(rsa.doFinal(data.getBytes(StandardCharsets.UTF_8))));
            return ecrypteddata;
    }

    public String generateDecryptedData(final byte[] encryptedData) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
            KeyStoreException, CertificateException, IOException, UnrecoverableKeyException {
            Base64 base64 = new Base64();
            X509Certificate cert;
            KeyStore keystore = loadKeyStore();
            cert = (java.security.cert.X509Certificate) keystore.getCertificate(pubKeyAlias);
            Cipher rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            rsa.init(Cipher.DECRYPT_MODE, cert);
            byte[] decodedValue = base64.decode(encryptedData);
            byte[] decodedData = (rsa.doFinal(decodedValue));
            return new String(decodedData);
    }    
}
