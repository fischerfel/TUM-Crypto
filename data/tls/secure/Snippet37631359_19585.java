private SSLContext createSSLContext2() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException, KeyManagementException, NoSuchProviderException {
    String path = "c:/tmp/example.crt2";
    byte[] certbytes = Files.readAllBytes(Paths.get(path));

    ByteArrayInputStream derInputStream = new ByteArrayInputStream(certbytes);
    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
    X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(derInputStream);
    String alias = cert.getSubjectX500Principal().getName();

    KeyStore trustStore = KeyStore.getInstance("JKS");
    trustStore.load(null, "".toCharArray());
    trustStore.setCertificateEntry(alias, cert);
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(trustStore, null);
    KeyManager[] keyManagers = kmf.getKeyManagers();

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(trustStore);
    TrustManager[] trustManagers = tmf.getTrustManagers();

    SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
    sslContext.init(keyManagers, trustManagers, null);
    return sslContext;
}
