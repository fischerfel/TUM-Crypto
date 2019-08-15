              TrustManager[] trustAllCerts = new TrustManager[]
               {
                 new X509TrustManager()
                  {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers()  { return null; }
                    public void checkClientTrusted( java.security.cert.X509Certificate[] certs, String authType)  {}
                    public void checkServerTrusted( java.security.cert.X509Certificate[] certs, String authType)  {}
                  }
                 };
              try
                {
                  SSLContext sc = SSLContext.getInstance( "SSL"); // "TLS" "SSL"
                  sc.init( null, trustAllCerts, null);
                  HttpsURLConnection.setDefaultSSLSocketFactory( sc.getSocketFactory());
                  HttpsURLConnection.setDefaultHostnameVerifier( 
                   new HostnameVerifier() 
                    {
                      public boolean verify( String hostname, SSLSession session) { return true; }
                    } );
                }
               catch( Exception e)
