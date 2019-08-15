TrustManager[] trustAllCerts = new TrustManager[] {
    new X509TrustManager() {
      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
       return null;
      }
      @Override
      public void checkClientTrusted(X509Certificate[] arg0, String arg1)
       throws CertificateException {}

      @Override
      public void checkServerTrusted(X509Certificate[] arg0, String arg1)
        throws CertificateException {}

      }
 };

  SSLContext sc=null;
  try {
   sc = SSLContext.getInstance("SSL");
  } catch (NoSuchAlgorithmException e) {
   e.printStackTrace();
  }
  try {
   sc.init(null, trustAllCerts, new java.security.SecureRandom());
  } catch (KeyManagementException e) {
   e.printStackTrace();
  }
  HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

  // Create all-trusting host name verifier
  HostnameVerifier validHosts = new HostnameVerifier() {
  @Override
 public boolean verify(String arg0, SSLSession arg1) {
   return true;
  }
  };
  // All hosts will be valid
  HttpsURLConnection.setDefaultHostnameVerifier(validHosts);
