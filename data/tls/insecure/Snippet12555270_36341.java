TrustManagerFactory tmf = TrustManagerFactory.getInstance(
    TrustManagerFactory.getDefaultAlgorithm());
tmf.init((KeyStore)null);
TrustManager[] trustManagers = tmf.getTrustManagers();

for (int i = 0; i < trustManagers.length; i++) {
    if (trustManagers[i] instanceof X509TrustManager) {
       trustManagers[i] = IgnoreClientUsageTrustManager(trustManagers[i]);
    }
}

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(... you keymanagers ..., trustManagers, null);
