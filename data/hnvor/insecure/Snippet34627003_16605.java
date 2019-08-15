public class MainActivity extends AppCompatActivity {

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // cert file stored in \app\src\main\res\raw
            InputStream caInput = getResources().openRawResource(R.raw.your_cert);

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

            AsyncSSLSocketMiddleware sslMiddleWare = Ion.getDefault(mContext).getHttpClient().getSSLSocketMiddleware();
            sslMiddleWare.setTrustManagers(wrappedTrustManagers);
            sslMiddleWare.setHostnameVerifier(getHostnameVerifier());
            sslMiddleWare.setSSLContext(sslContext);

            // Post application/x-www-form-urlencoded and read a String
            Ion.with(mContext)
                    .load("https://yourserver/token")
                    .setBodyParameter("grant_type", "password")
                    .setBodyParameter("username", "bnk")
                    .setBodyParameter("password", "bnk123456789")
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if (result != null) {
                                Log.i("ionSample", result);
                            } else if (e != null) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
                // or the following:
                // HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                // return hv.verify("www.yourserver.com", session);
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
}
