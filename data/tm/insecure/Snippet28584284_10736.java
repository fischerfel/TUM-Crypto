package com.ade.martini;

import org.apache.axis.components.net.BooleanHolder;
import org.apache.axis.components.net.SecureSocketFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Hashtable;

public class YourSecureSocketFactory implements SecureSocketFactory {

private String KEYSTORE_TYPE = "JKS";
private String PROTOCOL = "SSL";
private String ALGORITHM = "SunX509";
private int DEFAULT_PORT = 443;

protected SSLSocketFactory sslFactory = null;
private String keystoreFile;
private String keystorePass;
private KeyStore kstore = null;

public AxisSecureSocketFactory(Hashtable attributes) {
}

public Socket create(String host, int port, StringBuffer otherHeaders, BooleanHolder useFullURL)
        throws Exception {

    if (sslFactory == null) initFactory();

    if (port<=0) port = DEFAULT_PORT;
    Socket sslSocket = sslFactory.createSocket(host, port);
    return sslSocket;
}

protected void initFactory() throws IOException {

    /* One way to find certificates */
    keystoreFile = "yourkeystore.jks";
    keystorePass = "password";

    try {
        SSLContext sslContext = getContext();
        sslFactory = sslContext.getSocketFactory();
    } catch (Exception e) {
        if (e instanceof IOException) {
            throw (IOException) e;
        }
        throw new IOException(e.getMessage());
    }
}

/* this method will accept all certificates and therefore should NOT
   be used in most production settings */
private TrustManager[ ] getTrustManagers() {
    TrustManager[ ] certs = new TrustManager[ ] {
            new X509TrustManager() {
                public X509Certificate[ ] getAcceptedIssuers() { return null; }
                public void checkClientTrusted(X509Certificate[ ] certs, String t) { }
                public void checkServerTrusted(X509Certificate[ ] certs, String t) { }
            }
    };
    return certs;
}

protected SSLContext getContext() throws Exception {

    kstore = initKeyStore(keystoreFile, keystorePass);
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(ALGORITHM);
    kmf.init(kstore, keystorePass.toCharArray());
    SSLContext sslContext = SSLContext.getInstance(PROTOCOL);
    sslContext.init(kmf.getKeyManagers(), getTrustManagers(), new SecureRandom());
    return (sslContext);
}

private KeyStore initKeyStore(String keystoreFile, String keystorePass)
        throws IOException {
    try {
        KeyStore kstore = KeyStore.getInstance(KEYSTORE_TYPE);
        ClassLoader classLoader = YourSecureSocketFactory.class.getClassLoader();
        File file = new File(classLoader.getResource(keystoreFile).getFile());
        InputStream istream = new FileInputStream(file);
        kstore.load(istream, keystorePass.toCharArray());
        return kstore;
    } catch (FileNotFoundException fnfe) {
        throw fnfe;
    } catch (IOException ioe) {
        throw ioe;
    } catch (Exception ex) {
        ex.printStackTrace();
        throw new IOException("Exception trying to load keystore " + keystoreFile + ": " + ex.getMessage());
    }
}}
