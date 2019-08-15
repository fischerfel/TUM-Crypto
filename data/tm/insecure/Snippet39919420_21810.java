SSLContext sslContext = null;

if (testWitCa) {

    Certificate ca = null;
    try {
        ca = cf.generateCertificate(caInput);
        Log.v(this, "ca = " + ((X509Certificate) ca).getSubjectDN());
    } finally {
        caInput.close();
    }

    // Create a KeyStore containing our trusted CAs
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);

    // Create a TrustManager that trusts the CAs in our KeyStore
    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);

    // Create an SSLContext that uses our TrustManager
    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, tmf.getTrustManagers(), null);

} else {

    final TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    Log.v(FileDownloader.this, "Checking client with %s", authType);
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    Log.v(FileDownloader.this, "Checking server with %s", authType);
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
    };

    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, trustAllCerts, null);
}

URL url = new URL(fromUrl);
HttpURLConnection httpConn;

httpConn = (HttpsURLConnection) url.openConnection();

if (sslContext != null) {
    ((HttpsURLConnection) httpConn).setSSLSocketFactory(sslContext.getSocketFactory());
}

responseCode = httpConn.getResponseCode();
