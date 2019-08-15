package com.adamiworks.commonutils.ldap;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * This class accept all SSL Certificates even if it can assure its
 * Certification Institute.
 * 
 * DO NOT USE AT PRODUCTION ENVIRONMENTS
 * 
 * @author Tiago J. Adami
 *
 */
public class TrustAllCertificatesSSLSocketFactory extends SocketFactory {
    private SocketFactory socketFactory;

    public TrustAllCertificatesSSLSocketFactory() {
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(null, new TrustManager[] { new AllCertificatesTrustManager() }, new SecureRandom());
            socketFactory = ctx.getSocketFactory();
        } catch (Exception ex) {
            ex.printStackTrace(System.err); /* handle exception */
        }
    }

    public static SocketFactory getDefault() {
        return new TrustAllCertificatesSSLSocketFactory();
    }

    @Override
    public Socket createSocket(String string, int i) throws IOException, UnknownHostException {
        return socketFactory.createSocket(string, i);
    }

    @Override
    public Socket createSocket(String string, int i, InetAddress ia, int i1) throws IOException, UnknownHostException {
        return socketFactory.createSocket(string, i, ia, i1);
    }

    @Override
    public Socket createSocket(InetAddress ia, int i) throws IOException {
        return socketFactory.createSocket(ia, i);
    }

    @Override
    public Socket createSocket(InetAddress ia, int i, InetAddress ia1, int i1) throws IOException {
        return socketFactory.createSocket(ia, i, ia1, i1);
    }

    private class AllCertificatesTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            // do nothing
        }

        public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            // do nothing
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }
    }
}
