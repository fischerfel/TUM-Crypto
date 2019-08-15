private static SSLSocketFactory
getSSLSocketFactory(Context context)
        throws CertificateException,
        NoSuchAlgorithmException,
        KeyStoreException,
        KeyManagementException,
        IOException{

    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
    InputStream inputStream = context.getResources().openRawResource(R.raw.sslcrt);

    // Error
    Certificate certificate = certificateFactory.generateCertificate(inputStream);

    String keyType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", certificate);
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(keyStore);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
    return sslContext.getSocketFactory();
}
