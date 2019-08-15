OkHttpClient client = new OkHttpClient();
        try {
            KeyStore keyStore = SSLUtils.getKeyStore(applicationContext);
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null,trustManagerFactory.getTrustManagers(), new SecureRandom());
            client.setSslSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            Log.d("AppName", "cannot create http client", e);
        }
