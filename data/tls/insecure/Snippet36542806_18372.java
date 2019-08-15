 public static SSLContext getSSL() {
    try {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        AssetManager assetManager = App.getAppContext()
                .getAssets();
        InputStream caInput = assetManager.open("cert.pem");
        java.security.cert.X509Certificate ca = null;
        try {
            ca = (java.security.cert.X509Certificate) cf
                    .generateCertificate(caInput);
        } catch (Exception er) {
        } finally {
            caInput.close();
        }
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca",
                ca);
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(tmfAlgorithm);
        tmf.init(keyStore);
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        return context;
    } catch (Exception e1) {
        return null;
    }
}
