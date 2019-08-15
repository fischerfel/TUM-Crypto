class SSLTrustManager
{
  private X509TrustManager origTrustmanager;

  public SSLTrustManager()
  {
    try
    {
      TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      tmf.init((KeyStore) null);
      TrustManager[] trustManagers = tmf.getTrustManagers();
      this.origTrustmanager = (X509TrustManager) trustManagers[0];
    }
    catch (Exception ex)
    {
    }
  }

  public javax.net.ssl.SSLSocketFactory GetSocketFactory()
  {
    try
    {
      TrustManager[] wrappedTrustManagers = new TrustManager[] {
          new X509TrustManager()
          {
            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
              return origTrustmanager.getAcceptedIssuers();
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType)
            {
              try
              {
                origTrustmanager.checkClientTrusted(certs, authType);
              }
              catch (CertificateException e)
              {
              }
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException
            {
              try
              {
                origTrustmanager.checkServerTrusted(certs, authType);
              }
              catch(Exception ex)
              {
              }
            }
          }
       };

      SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, wrappedTrustManagers, new java.security.SecureRandom());
      javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
      return sslSocketFactory;
    }
    catch (Exception ex)
    {
      return null;
    }
  }
}    
