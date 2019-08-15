TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
       TrustManagerFactory.getDefaultAlgorithm());
   trustManagerFactory.init((KeyStore) null);
   TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
   if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
     throw new IllegalStateException("Unexpected default trust managers:"
         + Arrays.toString(trustManagers));
   }
   X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

   SSLContext sslContext = SSLContext.getInstance("TLS");
   sslContext.init(null, new TrustManager[] { trustManager }, null);
   SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

   OkHttpClient client = new OkHttpClient.Builder()
       .sslSocketFactory(sslSocketFactory, trustManager);
       .build();
