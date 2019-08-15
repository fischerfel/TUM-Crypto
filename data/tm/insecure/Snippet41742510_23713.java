import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;

public class ClientHelper {

public static Client configureClient() {
    TrustManager[] certs = new TrustManager[] { new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    } };

    SSLContext ctx = null;
    try {
        ctx = SSLContext.getInstance("TLSv1.2");
        ctx.init(null, certs, new SecureRandom());
    } catch (java.security.GeneralSecurityException e) {

    }

    HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

    ClientBuilder clientBuilder = ClientBuilder.newBuilder();
    try {
        clientBuilder.sslContext(ctx);
        clientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    } catch (Exception e) {
    }

    return clientBuilder.withConfig(new ClientConfig()).build();
}
