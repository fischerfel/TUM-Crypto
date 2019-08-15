mRequestQueue = Volley.newRequestQueue(this, hurlStack);

private HurlStack hurlStack = new HurlStack()
{
    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException
    {
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
        try
        {
            httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
            httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
        }
        catch (Exception e)
        {
            AppUtils.printLog(Log.ERROR, TAG, e.getMessage());
        }
        return httpsURLConnection;
    }
};

private SSLSocketFactory getSSLSocketFactory() throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException
{
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    InputStream caInput = getResources().openRawResource(R.raw.keystore); // this cert file stored in \app\src\main\res\raw folder path

    Certificate ca = cf.generateCertificate(caInput);
    caInput.close();

    KeyStore keyStore = KeyStore.getInstance("BKS");
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);

    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);

    TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, wrappedTrustManagers, null);

    return sslContext.getSocketFactory();
}

// Let's assume your server app is hosting inside a server machine
// which has a server certificate in which "Issued to" is "localhost",for example.
// Then, inside verify method you can verify "localhost".
// If not, you can temporarily return true
private HostnameVerifier getHostnameVerifier()
{
    return new HostnameVerifier()
    {
        @Override
        public boolean verify(String hostname, SSLSession session)
        {
            //return true; // verify always returns true, which could cause insecure network traffic due to trusting TLS/SSL server certificates for wrong hostnames
            HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
            return hv.verify("localhost", session);
        }
    };
}

private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers)
{
    final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
    return new TrustManager[] {new X509TrustManager()
    {
        public X509Certificate[] getAcceptedIssuers()
        {
            return originalTrustManager.getAcceptedIssuers();
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType)
        {
            try
            {
                if (certs != null && certs.length > 0)
                {
                    certs[0].checkValidity();
                }
                else
                {
                    originalTrustManager.checkClientTrusted(certs, authType);
                }
            }
            catch (CertificateException e)
            {
                Log.w("checkClientTrusted", e.toString());
            }
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType)
        {
            try
            {
                if (certs != null && certs.length > 0)
                {
                    certs[0].checkValidity();
                }
                else
                {
                    originalTrustManager.checkServerTrusted(certs, authType);
                }
            }
            catch (CertificateException e)
            {
                Log.w("checkServerTrusted", e.toString());
            }
        }
    }};
}
