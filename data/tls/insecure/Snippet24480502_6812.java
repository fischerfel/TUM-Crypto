public static void setSelfSignedCertSSLContext(AssetManager assets)
        throws Exception {
    // Load self-signed cert from an InputStream
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    InputStream caInput = assets.open("selfSigned.cer");
    Certificate ca;
    try {
        ca = cf.generateCertificate(caInput);
        Log.d(LOG_TAG, "ca=" + ((X509Certificate) ca).getSubjectDN());
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

    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
            .getDefaultAlgorithm());
    kmf.init(keyStore, "changeit".toCharArray());

    // Create an SSLContext that uses our TrustManager
    SSLContext context = SSLContext.getInstance("TLS");
    SSLContext.setDefault(context);

    context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    HttpsURLConnection.setDefaultSSLSocketFactory(context
            .getSocketFactory());

    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        public boolean verify(String string, SSLSession ssls) {
            return true;
        }
    });

    Log.d(LOG_TAG, "SSLContext set successfully");
}
