package gem_test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.security.pkcs11.SunPKCS11;

public class Test {
    private static final String ALGORITHM = "RSA";

    static int hard_soft = 1; // 1 - smart card, 2 - soft certificate
    static int sign_encrypt = 2; // 1- sign, 2 - encryption


    public static void main(String[] args) throws Exception {
        PrivateKey privateKey;
        PublicKey pubKey;
        if (hard_soft == 1) {
            String pkcsConf = (
                "name = Personal\n" +
                "library = /usr/local/lib/personal/libP11.so\n" +
//                    "library = c:\\perso\\bin\\personal.dll\n" +
                "slot = 0\n"
            );

            char[] pin = "123456".toCharArray();
            String useCertAlias = "Digital Signature";
//                String useCertAlias = "Non Repudiation";

            SunPKCS11 provider = new SunPKCS11(new ByteArrayInputStream(pkcsConf.getBytes()));
            String providerName = provider.getName();
            Security.addProvider(provider);

            KeyStore keyStore = KeyStore.getInstance("PKCS11", providerName);
            keyStore.load(null, pin);

            privateKey = (PrivateKey) keyStore.getKey(useCertAlias, pin);
            X509Certificate certificate = (X509Certificate) keyStore.getCertificate(useCertAlias);
            pubKey = certificate.getPublicKey();
        } else if (hard_soft == 2) {
            /*
             mkdir /tmp/softkey
             cd /tmp/softkey

             openssl genrsa 2048 > softkey.key
             chmod 400 softkey.key
             openssl req -new -x509 -nodes -sha1 -days 365 -key softkey.key -out softkey.crt
             openssl pkcs12 -export -in softkey.crt -inkey softkey.key -out softkey.pfx
             rm -f softkey.key softkey.crt
             */
            String pfx = "/tmp/softkey/softkey.pfx";
            String useCertAlias = "1";

            KeyStore keyStore1 = KeyStore.getInstance("PKCS12");
            keyStore1.load(new FileInputStream(pfx), new char[]{});

            privateKey = (PrivateKey) keyStore1.getKey(useCertAlias, new char[]{});
            X509Certificate certificate = (X509Certificate) keyStore1.getCertificate(useCertAlias);
            pubKey = certificate.getPublicKey();
        } else {
            throw new IllegalStateException();
        }

        if (sign_encrypt == 1) {
            byte[] sig = signDocument("msg content".getBytes(), privateKey);
            boolean result = verifyDocument("msg content".getBytes(), sig, pubKey);
            System.out.println("RESULT " + result);
        } else if (sign_encrypt == 2) {
            byte[] encrypted = encryptDocument("msg content".getBytes(), pubKey);
            byte[] decryptedDocument = decryptDocument(encrypted, privateKey);
            System.out.println("RESULT " + new String(decryptedDocument));
        } else {
            throw new IllegalStateException();
        }
    }

    private static byte[] signDocument(byte[] aDocument, PrivateKey aPrivateKey) throws Exception {
        Signature signatureAlgorithm = Signature.getInstance("SHA1withRSA");
        signatureAlgorithm.initSign(aPrivateKey);
        signatureAlgorithm.update(aDocument);
        byte[] digitalSignature = signatureAlgorithm.sign();
        return digitalSignature;
    }

    private static boolean verifyDocument(byte[] aDocument, byte[] sig, PublicKey pubKey) throws Exception {
        Signature signatureAlgorithm = Signature.getInstance("SHA1withRSA");
        signatureAlgorithm.initVerify(pubKey);
        signatureAlgorithm.update(aDocument);
        return signatureAlgorithm.verify(sig);
    }


    private static byte[] encryptDocument(byte[] aDocument, PublicKey pubKey) throws Exception {
        int encrypt_wrap = 2;
        if (encrypt_wrap == 1) {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.PUBLIC_KEY, pubKey);
            return cipher.doFinal(aDocument);
        } else if (encrypt_wrap == 2) {
            SecretKey data = new SecretKeySpec(aDocument, 0, aDocument.length, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.WRAP_MODE, pubKey);
            return cipher.wrap(data);
        } else {
            throw new IllegalStateException();
        }
    }

    public static byte[] decryptDocument(byte[] encryptedDocument, PrivateKey aPrivateKey) throws Exception {
        int encrypt_wrap = 2;
        if (encrypt_wrap == 1) {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.PRIVATE_KEY, aPrivateKey);
            return cipher.doFinal(encryptedDocument);
        } else if (encrypt_wrap == 2) {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.UNWRAP_MODE, aPrivateKey);
            SecretKey res = (SecretKey) cipher.unwrap(encryptedDocument, "AES", Cipher.SECRET_KEY);
            return res.getEncoded();
        } else {
            throw new IllegalStateException();
        }
    }
}
