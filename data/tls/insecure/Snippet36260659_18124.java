public static HttpsURLConnection setupHttpsConnection(URL url, Context context) {
    try {
        // Load CAs from an InputStream
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = new BufferedInputStream(context.getAssets().open("localhost.crt"));
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            Log.d(LOG_TAG, "> setupHttpsConnection > ca.getSubjectDN = " + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        /*
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv =
                        HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify("192.168.0.121", session);
            }
        };
        */

        // Tell the URLConnection to use a SocketFactory from our SSLContext
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        //urlConnection.setHostnameVerifier(hostnameVerifier);
        urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
        return urlConnection;

    } catch (Exception e) {
        e.printStackTrace();
        Log.d(LOG_TAG, e.toString());
        return null;
    }
}
