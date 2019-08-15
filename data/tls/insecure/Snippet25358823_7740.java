try {
    // Read the certificate bundle from disk
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    Certificate ca = cf.generateCertificate(getClass().getResourceAsStream("ca-bundle.crt")); // the file ca-bundle.crt should be in the same folder as your .java files
    // Create a KeyStore containing our trusted CAs
    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);
    // Create a TrustStore that trusts the CAs in our KeyStore
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(keyStore);
    // Create an SSLContext that uses our TrustManager
    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, tmf.getTrustManagers(), null);
} catch (CertificateException | KeyStoreException | NoSuchAlgorithmException | KeyManagementException | IOException e) {
        e.printStackTrace();
}
