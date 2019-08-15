...
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    try{
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(getSSLSocketFactory())
                .hostnameVerifier(getHostnameVerifier())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        WebAPIService service = retrofit.create(WebAPIService.class);

        Call<JsonObject> jsonObjectCall = service.getData(...);
        ...
    } catch (Exception e) {
        e.printStackTrace();
    }
}

// for SSL...    
// Read more at https://developer.android.com/training/articles/security-ssl.html#CommonHostnameProbs
private HostnameVerifier getHostnameVerifier() {
    return new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true; // verify always returns true, which could cause insecure network traffic due to trusting TLS/SSL server certificates for wrong hostnames
            //HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
            //return hv.verify("localhost", session);
        }
    };
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
                        if (certs != null && certs.length > 0){
                            certs[0].checkValidity();
                        } else {
                            originalTrustManager.checkClientTrusted(certs, authType);
                        }
                    } catch (CertificateException e) {
                        Log.w("checkClientTrusted", e.toString());
                    }
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    try {
                        if (certs != null && certs.length > 0){
                            certs[0].checkValidity();
                        } else {
                            originalTrustManager.checkServerTrusted(certs, authType);
                        }
                    } catch (CertificateException e) {
                        Log.w("checkServerTrusted", e.toString());
                    }
                }
            }
    };
}

private SSLSocketFactory getSSLSocketFactory()
        throws CertificateException, KeyStoreException, IOException,
        NoSuchAlgorithmException, KeyManagementException {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    InputStream caInput = getResources().openRawResource(R.raw.your_cert); // File path: app\src\main\res\raw\your_cert.cer
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
...
