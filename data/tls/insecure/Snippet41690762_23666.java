 package org;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class elkclient {

public static void main(String[] args) throws CertificateException,   KeyStoreException, NoSuchAlgorithmException, IOException, KeyManagementException     {
                  // Load Certificate
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                Certificate certificate = certificateFactory.generateCertificate(new FileInputStream(new File("C:/Work/certi/jre1.8.0_91/lib/security/certificate.cer")));

                // Create TrustStore
                KeyStore trustStoreContainingTheCertificate = KeyStore.getInstance("JKS");
                trustStoreContainingTheCertificate.load(null, null);

                trustStoreContainingTheCertificate.setCertificateEntry("cert", certificate);

                // Create SSLContext
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(trustStoreContainingTheCertificate);

                SSLContext sslContext =  SSLContext.getInstance("TLS");
                sslContext.init(null, trustManagerFactory.getTrustManagers(), null);


                 HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;                 
                 SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext,hostnameVerifier);

                 HttpClientBuilder builder = HttpClientBuilder.create();
                  builder.setSSLSocketFactory(sslSocketFactory);
                  builder.build();

                  CloseableHttpClient httpclient = builder.build();

                  HttpGet httpget = new HttpGet("https://server-rest-link/");
                  CloseableHttpResponse response = httpclient.execute(httpget);

            }
}
