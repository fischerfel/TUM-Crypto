public static SSLSocketFactory newSslSocketFactory(Context conText) {

    Certificate ca = null;
    KeyStore keyStore;
    TrustManagerFactory tmf = null;
    SSLContext context = null;

    InputStream caInput = null;

    try {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        //R.raw.root contain ROOT File which is given BY Certificate authority
        caInput = conText.getResources().openRawResource(R.raw.root);

        try {
            ca = cf.generateCertificate(caInput);

            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } catch (CertificateException e) {
        }
    } catch (CertificateException e) {

    }
    String keyStoreType = KeyStore.getDefaultType();
    keyStore = null;

    try {
        keyStore = KeyStore.getInstance(keyStoreType);

        try {
            keyStore.load(null,null);
        } catch (IOException e) {


        } catch (NoSuchAlgorithmException e) {

        } catch (CertificateException e) {
        }
        keyStore.setCertificateEntry("ca", ca);
    } catch (KeyStoreException e) {
    }

    // Create a TrustManager that trusts the CAs in our KeyStore
    try {
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);
    } catch (NoSuchAlgorithmException e) {

    } catch (KeyStoreException e) {

    }
    try {
        context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
    } catch (NoSuchAlgorithmException e) {

    } catch (KeyManagementException e) {

    }

    return context.getSocketFactory();

}
