KeyStore keyStore = ...;
   TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
   tmf.init(keyStore);

   SSLContext context = SSLContext.getInstance("TLS");
   context.init(null, tmf.getTrustManagers(), null);

   URL url = new URL("https://www.example.com/");
   HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
   urlConnection.setSSLSocketFactory(context.getSocketFactory());
   InputStream in = urlConnection.getInputStream();
