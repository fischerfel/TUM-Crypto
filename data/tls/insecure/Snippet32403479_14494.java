private static SSLSocketFactory getSocketFactory() {
    SSLSocketFactory socketFactory = null;


    try {
        final CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        final BufferedInputStream bis = new BufferedInputStream(sContext.getResources().openRawResource(R.raw.cert)) ;

        Certificate certificate;
        try {
            certificate = certificateFactory.generateCertificate(bis);
        } finally {
            bis.close();
        }

        final String keyStoreType = KeyStore.getDefaultType();
        final KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", certificate);

        final String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        socketFactory = sslContext.getSocketFactory();
    } catch (Exception e) {
        Log.w(TAG, Log.getStackTraceString(e));
    }
    return socketFactory;
}
