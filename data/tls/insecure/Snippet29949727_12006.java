   TrustManagerFactory tmf = TrustManagerFactory
     .getInstance(TrustManagerFactory.getDefaultAlgorithm());
   // Using null here initialises the TMF with the default trust store.
   tmf.init((KeyStore) null);

   // Get hold of the default trust manager
   X509TrustManager defaultTm = null;
   for (TrustManager tm : tmf.getTrustManagers()) {
    if (tm instanceof X509TrustManager) {
     defaultTm = (X509TrustManager) tm;
     break;
    }
   }
   //

   

   SSLContext sslContext = SSLContext.getInstance("TLS");
   sslContext.init(null, new TrustManager[] { defaultTm }, null);

   // You don't have to set this as the default context,
   // it depends on the library you're using.
   SSLContext.setDefault(sslContext);

   
   // Not sure that you need this but add it
   HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
     .getSocketFactory());
        