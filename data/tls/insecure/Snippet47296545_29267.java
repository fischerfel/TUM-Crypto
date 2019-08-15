    public SSLContext getSSLContext(String alias, String certificateString) throws Exception {

    SSLContext sc = SSLContext.getInstance("SSLv3");
        KeyStore ks = keyStoreFromCertificateString("xm",certificateString);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        sc.init(null, tmf.getTrustManagers(), null);
    }

    private static KeyStore keyStoreFromCertificateString(String alias, String certificateString) throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    ks.load(null); // Create empty key store
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    Certificate cert = cf.generateCertificate(
                    new ByteArrayInputStream(certificateString.getBytes()));
    ks.setCertificateEntry(alias, cert);
    return ks;
}
