private SSLSocketFactory getSSLSocketFactory()
        throws CertificateException, KeyStoreException, IOException,
        NoSuchAlgorithmException, KeyManagementException {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    InputStream caInput = getResources().openRawResource(R.raw.iis_cert);
    Certificate ca = cf.generateCertificate(caInput);
    caInput.close();
    KeyStore keyStore = KeyStore.getInstance("BKS");
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);
    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, tmf.getTrustManagers(), null);
    return sslContext.getSocketFactory();
}

private HostnameVerifier getHostnameVerifier() {
    return new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {            
            HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
            return hv.verify("BNK-PC.LOCALHOST.COM", session);
        }
    };
}
