package com.example.encryptiontest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.x509.X509V1CertificateGenerator;

import android.content.Context;
import android.util.Base64;

public class Encryption {
    private static final String ALIAS = "myAlias";
    private static final String FILE_NAME="key_store";
    private Context mContext;
    public Encryption(Context context){
        mContext = context;
    }
    public void save() throws NoSuchAlgorithmException, InvalidKeySpecException, CertificateEncodingException, InvalidKeyException, NoSuchProviderException, SignatureException, KeyStoreException, CertificateException, FileNotFoundException, UnsupportedEncodingException, IOException{
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();

        KeyFactory fact = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(),
                RSAPublicKeySpec.class);
        RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(),
                RSAPrivateKeySpec.class);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(pub.getModulus(),
                pub.getPublicExponent());
        PublicKey pubKey = fact.generatePublic(keySpec);
        PrivateKey privateKey = fact.generatePrivate(new RSAPrivateKeySpec(
                priv.getModulus(), priv.getPrivateExponent()));

        saveToKeyStore(pubKey,ALIAS,mContext.getFilesDir() + FILE_NAME,privateKey);
    }

    public void load() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableEntryException{
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] password = generatePassword(ALIAS);
        FileInputStream in = new FileInputStream(mContext.getFilesDir() + FILE_NAME);
        keyStore.load(in, password);
        KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) keyStore
                .getEntry(ALIAS, null);
        PrivateKey pubKey = (PrivateKey) keyEntry.getPrivateKey();
    }

    private void saveToKeyStore(PublicKey publicKey, String alias,
            String fileName, PrivateKey privateKey)
            throws CertificateEncodingException, NoSuchProviderException,
            NoSuchAlgorithmException, SignatureException, InvalidKeyException,
            KeyStoreException, IOException, CertificateException,
            FileNotFoundException, UnsupportedEncodingException {
        X509Certificate cert = getCertificate(publicKey, privateKey);
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] password = generatePassword(alias);

        keyStore.load(null);
        keyStore.setKeyEntry(alias, privateKey, null,
                new Certificate[] { cert });
        FileOutputStream out = new FileOutputStream(fileName);

        keyStore.store(out, password);
        out.close();
    }

    private X509Certificate getCertificate(PublicKey publicKey,
            PrivateKey privateKey) throws CertificateEncodingException,
            NoSuchProviderException, NoSuchAlgorithmException,
            SignatureException, InvalidKeyException {
        Date startDate = new Date(); // time from which certificate is valid
        Date expiryDate = new Date(2050, 3, 3); // time after which certificate
                                                // is not valid
        BigInteger serialNumber = BigInteger.ONE; // serial number for
                                                    // certificate
        KeyPair keyPair = new KeyPair(publicKey, privateKey); // EC
                                                                // public/private
                                                                // key pair
        X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
        X500Principal dnName = new X500Principal("CN=Test CA Certificate");
        certGen.setSerialNumber(serialNumber);
        certGen.setIssuerDN(dnName);
        certGen.setNotBefore(startDate);
        certGen.setNotAfter(expiryDate);
        certGen.setSubjectDN(dnName); // note: same as issuer
        certGen.setPublicKey(keyPair.getPublic());
        certGen.setSignatureAlgorithm("SHA512withRSA");
        X509Certificate cert = certGen.generate(keyPair.getPrivate(), "BC");
        return cert;
    }

    private char[] generatePassword(String userName)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String defaultUdid = android.provider.Settings.System.getString(
                mContext.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        MessageDigest md = MessageDigest.getInstance("SHA-512");

        byte[] bSalt = defaultUdid.getBytes("UTF-8");
        byte[] bPw = userName.getBytes("UTF-8");
        md.update(bPw);
        byte[] r = md.digest(bSalt);
        return Base64.encodeToString(r, Base64.URL_SAFE).toCharArray();
    }
}
