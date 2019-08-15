protected SSLSocketFactory getCertificateSocketFactory() throws Exception {

    InputStream certStrm = context.getResources().openRawResource(R.raw.cert_file)

    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    Certificate ca = cf.generateCertificate(certStrm);

    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);

    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, tmf.getTrustManagers(), null);

    return sslContext.getSocketFactory();

}
