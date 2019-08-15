public class AndroidSSLSocketFactory extends SSLSocketFactory {
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(
            "X509");
    public AndroidSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);
        tmf.init(truststore);
        TrustManager[] trustManagers = tmf.getTrustManagers();
        final X509TrustManager origTrustmanager = (X509TrustManager)trustManagers[0];
        TrustManager tm = new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return origTrustmanager.getAcceptedIssuers();
            }

            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain, String authType)
                    throws CertificateException {
                origTrustmanager.checkClientTrusted(chain, authType);
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain, String authType)
                    throws CertificateException {
                origTrustmanager.checkServerTrusted(chain, authType);
            }
        };
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{tm}, null);
    }
}
