SSLContext sslContext;
    TrustManager[] trustManagers;
    try {
        final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        final InputStream certInputStream = activity.getResources().openRawResource(R.raw.cert);
        InputStream avscertInputStream = activity.getResources().openRawResource(R.raw.amazon_avs_cert);
        final CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        final Certificate cert = certificateFactory.generateCertificate(certInputStream);
        final Certificate avscert = certificateFactory.generateCertificate(avscertInputStream);
        keyStore.setCertificateEntry("amazon_avs", avscert);
        keyStore.setCertificateEntry("cert", cert);
        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        trustManagers = trustManagerFactory.getTrustManagers();
        sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, trustManagers, null);
        return new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagers[0])
                .build();
    } catch (Exception e) {
        e.printStackTrace();
        return new OkHttpClient();
    }
