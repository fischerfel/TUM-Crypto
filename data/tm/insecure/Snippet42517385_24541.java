      X509TrustManager trustManager = new X509TrustManager() 
      {
          @Override
          public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException
          {
           //if (authType == null)
           //     throw new javax.security.cert.CertificateException();
          }

          @Override
          public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException
          {
          }

          @Override
          public java.security.cert.X509Certificate[] getAcceptedIssuers()
          {
               return new java.security.cert.X509Certificate[]{};
          }
        };
