package com.example.client;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public class InsecureTrustManager implements X509TrustManager {
    /**
     * {@inheritDoc}
     */
    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        // Everyone is trusted!
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        // Everyone is trusted!
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
