public static SSLContext getSslContext(Context context) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, NoSuchProviderException {
    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", "BC");
    InputStream inputStream = context.getResources().openRawResource(R.raw.keystore);

    Certificate certificate;

    try {
        certificate = certificateFactory.generateCertificate(inputStream);
    } finally {
        inputStream.close();
    }

    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", certificate);

    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
    trustManagerFactory.init(keyStore);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

    return sslContext;
}
