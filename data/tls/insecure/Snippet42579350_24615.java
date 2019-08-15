 private SSLSocketFactory getSSLSocketFactory()
        throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    InputStream caInput = getResources().openRawResource(R.raw.ms); // this cert file stored in \app\src\main\res\raw folder path

    Certificate ca = cf.generateCertificate(caInput);
    caInput.close();
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);

    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);


    //TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, tmf.getTrustManagers(), null);

    return sslContext.getSocketFactory();
}

private HostnameVerifier getHostnameVerifier() {
    return new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
}
