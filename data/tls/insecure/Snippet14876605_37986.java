KeyManagerFactory kmf = //... Initialise a KMF with your server's keystore

TrustManagerFactory tmf = TrustManagerFactory
    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init((KeyStore) null); // Use the default trust store
TrustManager[] trustManagers = tmf.getTrustManagers();
final X509TrustManager origTrustManager = (X509TrustManager) trustManagers[0];
final X509Certificate caCert = // Load your test CA certificate here.
X509TrustManager fakeTrustManager = new X509TrustManager() {
    public void checkClientTrusted(X509Certificate[] chain,
            String authType) throws CertificateException {
        // Key the behaviour of the default trust manager.
        origTrustManager.checkClientTrusted(chain, authType);
    }

    public void checkServerTrusted(X509Certificate[] chain,
            String authType) throws CertificateException {
        // Key the behaviour of the default trust manager.
        origTrustManager.checkServerTrusted(chain, authType);
    }

    public X509Certificate[] getAcceptedIssuers() {
        // This is only used for sending the list of acceptable CA DNs.
        return new X509Certificate[] { caCert };
    }
};
trustManagers = new X509TrustManager[] { fakeTrustManager };

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(kmf.getKeyManagers(), trustManagers, null);
