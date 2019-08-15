package net.milanaleksic.cuc.tools.async.http;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.*;

import org.apache.http.conn.scheme.*;
import org.apache.http.conn.ssl.X509HostnameVerifier;

public class HttpsSecurityOverride {

    private static SchemeRegistry allowAllSchemeRegistry = null;

    private static class AllowAllTrustManager implements X509TrustManager {

        @Override public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }

        @Override public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        @Override public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }

    private static class AllowAllHostnameVerifier implements X509HostnameVerifier {

        @Override public void verify(String arg0, SSLSocket arg1) throws IOException {
        }

        @Override public void verify(String arg0, X509Certificate arg1) throws SSLException {
        }

        @Override public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {
        }

        @Override public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }

    }

    public static SchemeRegistry createAllowAllSchemeRegistry() throws Exception {
        synchronized (HttpsSecurityOverride.class) {
            if (allowAllSchemeRegistry != null)
                return allowAllSchemeRegistry;

            SSLContext sslContext = SSLContext.getInstance("SSL");

            // set up a TrustManager that trusts everything
            sslContext.init(null, new TrustManager[] { new AllowAllTrustManager() }, new SecureRandom());

            org.apache.http.conn.ssl.SSLSocketFactory sf = new org.apache.http.conn.ssl.SSLSocketFactory(sslContext);
            sf.setHostnameVerifier(new AllowAllHostnameVerifier());
            Scheme httpsScheme = new Scheme("https", sf, 443);
            allowAllSchemeRegistry = new SchemeRegistry();
            allowAllSchemeRegistry.register(httpsScheme);

            return allowAllSchemeRegistry;
        }
    }

}
