SSLSocketFactory sslSocketFactory = getSSL(context);
URL url = new URL(address);
HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
urlConnection.setSSLSocketFactory(sslSocketFactory);
urlConnection.setDoOutput(true);
urlConnection.setDoInput(true);
urlConnection.connect();


protected static SSLSocketFactory getSSL(Context context) {
    SSLSocketFactory sslSocketFactory = null;
    try {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = context.getResources().getAssets().open("softRSAroot.cer");
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            Log.d("maggie", "ca= " + ((X509Certificate) ca).getSubjectDN());
        }finally {
            caInput.close();
        }
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        sslSocketFactory = sslContext.getSocketFactory();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return sslSocketFactory;
}
