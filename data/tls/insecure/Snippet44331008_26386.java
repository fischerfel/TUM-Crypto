 private SSLSocketFactory getSSLSocketFactory() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, CertificateException, IOException, UnrecoverableKeyException {


    CertificateFactory cf = CertificateFactory.getInstance("X.509");

    InputStream caInput = new BufferedInputStream(getAssets().open("xxxx.cer"));
    X509Certificate ca = null;
    try {
        ca = (X509Certificate)cf.generateCertificate(caInput);

        Log.v("TAG","ca = "+ca);

    } finally {
        caInput.close();
    }


    // Create a KeyStore containing our trusted CAs
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);
    String clientCertPassword = "xxxx!";

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
    kmf.init(keyStore, clientCertPassword.toCharArray());









    KeyStore trustStore = KeyStore.getInstance("PKCS12");
    InputStream caInput2 = new BufferedInputStream(getAssets().open("xxxx.pfx"));
    trustStore.load(caInput2, clientCertPassword.toCharArray());


    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(trustStore);



    KeyManager[] keyManagers = kmf.getKeyManagers();
    SSLContext sslContext = SSLContext.getInstance("SSL");



    TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

    X509TrustManager xtm = (X509TrustManager) trustManagers[0];

    for (X509Certificate cert : xtm.getAcceptedIssuers()) {
        String certStr = "S:" + cert.getSubjectDN().getName() + "\nI:"
                + cert.getIssuerDN().getName();

        Log.d("TAG", certStr);
    }



    sslContext.init(keyManagers,trustManagers,null);



    return sslContext.getSocketFactory();


}
