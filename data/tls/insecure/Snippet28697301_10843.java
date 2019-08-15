        InputStream in = new FileInputStream(new File("c:/temp/F5keystore"));
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(in, "changeit".toCharArray());
        in.close();

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        X509TrustManager defaultTrustManager = (X509TrustManager)tmf.getTrustManagers()[0];
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new TrustManager[] {defaultTrustManager}, null);
        SSLSocketFactory sslSocketFactory = context.getSocketFactory();

        ((HttpsURLConnection) m_httpsConnection).setSSLSocketFactory(sslSocketFactory);
