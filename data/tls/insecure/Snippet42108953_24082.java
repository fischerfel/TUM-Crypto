private final SSLContext createSSLContext()
        throws Exception {

    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    FileInputStream in = new FileInputStream("path to server certificate.pem"); // server certificate in PEM format
    KeyStore trustStore = KeyStore.getInstance("JKS");
    trustStore.load(null);
    try {
        X509Certificate cacert = (X509Certificate) cf.generateCertificate(in);
        trustStore.setCertificateEntry("server_alias", cacert);
    } finally {
        IOUtils.closeQuietly(in);
    }

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(trustStore);

    SSLContext sslContext = SSLContext.getInstance("SSL"); // TLS e.g.
    sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
    return sslContext;
}
