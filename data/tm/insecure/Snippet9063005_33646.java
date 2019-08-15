package org.xmlrpc.android;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

class EasyX509TrustManager implements X509TrustManager
{
    public EasyX509TrustManager(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
        super();

    }
@Override
public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
{

}

@Override
public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
{

}

@Override
public X509Certificate[] getAcceptedIssuers()
{
return new X509Certificate[0];
}
}
