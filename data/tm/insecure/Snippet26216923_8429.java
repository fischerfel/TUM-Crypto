  TrustManager[] trustAllCerts = new TrustManager[]
   { new X509TrustManager()
      {
        public java.security.cert.X509Certificate[] getAcceptedIssuers()  { return null; }
        public void checkClientTrusted( X509Certificate[] chain, String authType) {}
        public void checkServerTrusted( X509Certificate[] chain, String authType) {}
      }
   };

  try
    {
      SSLContext sc = SSLContext.getInstance( "SSL"); // "TLS" "SSL"
      sc.init( null, trustAllCerts, null);
      // sc.init( null, trustAllCerts, new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory( sc.getSocketFactory());
      HttpsURLConnection.setDefaultHostnameVerifier( 
       new HostnameVerifier() 
        {
          public boolean verify( String hostname, SSLSession session) { return true; }
        } );
    }
   catch (Exception e) {}
