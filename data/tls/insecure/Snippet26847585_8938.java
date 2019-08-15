private static SSLSocketFactory createPinnedSSLCertFactory(Context ctx) {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    Certificate myCert = //read certificate in;
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null); //inputstream null creates new keystore
    keyStore.setCertificateEntry("mycert", myCert );

    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);

    SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, tmf.getTrustManagers(), null);

    return context.getSocketFactory();
}
