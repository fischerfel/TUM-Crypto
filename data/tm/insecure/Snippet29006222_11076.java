   TrustManager[] trustAllCerts =
         new TrustManager[] { new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
               return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs,
                  String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs,
                  String authType) {
            }

         } };
   SSLContext sslcontext = null;
   try {
      sslcontext = SSLContext.getInstance("SSL");
      sslcontext.init(null, trustAllCerts, new java.security.SecureRandom());
   } catch (NoSuchAlgorithmException e) {
      logger.warn(e.getMessage());
   } catch (KeyManagementException e) {
      logger.warn(e.getMessage());
   }
   // Allow TLSv1 protocol only
   final SSLConnectionSocketFactory sslsf =
         new SSLConnectionSocketFactory(sslcontext,
               new String[] { "TLSv1" }, null, NoopHostnameVerifier.INSTANCE);

   Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
         .<ConnectionSocketFactory> create().register("https", sslsf)
         .build();
   PoolingHttpClientConnectionManager cm =
         new PoolingHttpClientConnectionManager(socketFactoryRegistry);

   // Increase max total connection to 200
   cm.setMaxTotal(200);
   // Increase default max connection per route to 20
   cm.setDefaultMaxPerRoute(20);
