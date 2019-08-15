package ch.debugging.server

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.encoders.Base64;

import javax.net.ssl.*;
import java.io.*;
import java.math.BigInteger;
import java.net.BindException;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;


public class Main {

    public static void main(String[] args) {

        KeyStore keystore = null;
        try {
            BouncyCastleProvider bc = new BouncyCastleProvider();
            File keystoreFile = new File("D:\\keystore.asdf");
            keystore = KeyStore.getInstance("JKS");

        //Create new keystore if there is none
            if (!keystoreFile.exists()) {
                SecureRandom r = SecureRandom.getInstanceStrong();

                KeyPairGenerator keygen = KeyPairGenerator.getInstance("EC", bc);
                keygen.initialize(384, r);
                KeyPair keys = keygen.generateKeyPair();
                System.out.println("Keys successfully generated");

                //Parameters for the certificate
                X500Name issuer = new X500Name("C=, ST=, L=, O=, OU=, CN=");
                X500Name subject = issuer; //self-signed certificate: issuer and subject are the same
                BigInteger serial = BigInteger.valueOf(r.nextLong());
                Date notBefore = new Date(System.currentTimeMillis());
                Date notAfter = new Date(System.currentTimeMillis() + (1000 * 3600 * 24 * 365 * 10)); //valid for one year from now

                //Build the certificate
                X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuer, serial, notBefore, notAfter, subject, keys.getPublic());
                ContentSigner signer = new JcaContentSignerBuilder("SHA512WithECDSA").build(keys.getPrivate());
                X509CertificateHolder certHolder = certGen.build(signer);
                X509Certificate[] certChain = {new JcaX509CertificateConverter().setProvider(bc).getCertificate(certHolder)};
                System.out.println(Base64.toBase64String(certChain[0].getEncoded()));
                System.out.println("Certificate built");

                //Save keys and the certificate to keystore
                keystore.load(null, "123456".toCharArray());
                keystore.setCertificateEntry("Certificate", certChain[0]);
                keystore.store(new FileOutputStream(keystoreFile), "123456".toCharArray());
                System.out.println("Certificate saved in keystore");
            }
        //load keystore if it exists
            else {
                keystore.load(new FileInputStream(keystoreFile), "123456".toCharArray());
                System.out.println("Keystore loaded");
            }
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            SecureRandom r = SecureRandom.getInstanceStrong();
            //Prepare SSLServerSocket
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            kmf.init(keystore,"123456".toCharArray());
            tmf.init(keystore);
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(kmf.getKeyManagers(),tmf.getTrustManagers(),r);

            //Create and set up server socket
            SSLServerSocketFactory factory = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(6060);

            serverSocket.setEnabledProtocols(new String[]{"TLSv1.2"});
            serverSocket.setEnabledCipherSuites(new String[]{"TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384"});
            serverSocket.setEnableSessionCreation(true);
            serverSocket.setNeedClientAuth(false);
            System.out.println("Waiting for incoming connections...");
            SSLSocket s = (SSLSocket) serverSocket.accept();
            s.startHandshake();
            System.out.println("------Connection established------");
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            while (true) {
                System.out.println(in.readLine());
            }
        } catch(BindException bind) {
            System.out.println("Failed to bind port 6060. Is it already in use?");
            bind.printStackTrace();
        } catch(IOException io) {
            io.printStackTrace();
        } catch(Exception kse) {
            kse.printStackTrace();
        }
    }
}
