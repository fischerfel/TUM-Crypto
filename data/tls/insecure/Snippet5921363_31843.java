   TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance("SunX509");
   trustManagerFactory.init(trustStore);
   trustManagers = trustManagerFactory.getTrustManagers();
   SSLContext context = SSLContext.getInstance("TLS");
   context.init(keyManagers, trustManagers, new SecureRandom());
   HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
