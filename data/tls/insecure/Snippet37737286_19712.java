/**
 * This is used to verify local host. Let's assume the app is hosted inside of a server
 * machine which has a server certificate in which "Issued to" is "localhost", then
 * this method will verify "localhost". If not, can temporarily return true
 * @return
 */
private HostnameVerifier getHostnameVerifier() {
    return new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            //return true;
            HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
            return hv.verify("localhost", session);
        }
    };
}

private SSLSocketFactory getSSLSocketFactory()
        throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {

    InputStream caInput = mContext.getResources().openRawResource(R.raw.gdig2); // this certificate file stored in \app\src\main\res\raw folder path
    // From https://www.washington.edu/itconnect/security/ca/load-der.crt
    //InputStream caInput = new BufferedInputStream(new FileInputStream("load-der.crt"));
    CertificateFactory cf = null;
    Certificate ca = null;
    try {
        cf = CertificateFactory.getInstance("X.509", "BC");
        ca = cf.generateCertificate(caInput);
        System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
    } catch (NoSuchProviderException e) {
        e.printStackTrace();
    } finally {
        caInput.close();
    }
    caInput.close();

    // Create a KeyStore containing our trusted CAs
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType); //KeyStore keyStore = KeyStore.getInstance("BKS");
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);

    // Create a TrustManager that trusts the CAs in our KeyStore
    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);

    TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

    // Create an SSLContext that uses our TrustManager
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, wrappedTrustManagers, null);

    return sslContext.getSocketFactory();
}

private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
    final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
    return new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return originalTrustManager.getAcceptedIssuers();
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    try {
                        originalTrustManager.checkClientTrusted(certs, authType);
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    }
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    try {
                        originalTrustManager.checkServerTrusted(certs, authType);
                    } catch (CertificateException e) {
                        // THIS IS THE EXCEPTION I GET WITH EACH REQUEST MADE
                        e.printStackTrace();
                    }
                }
            }
    };
}
