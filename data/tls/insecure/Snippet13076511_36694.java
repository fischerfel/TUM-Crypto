public static void disableCertificateValidation() 
{
    // Create a trust manager that does not validate certificate chains
      TrustManager[] trustAllCerts = new TrustManager[] { 
        new X509TrustManager() {
          public X509Certificate[] getAcceptedIssuers() { 
            return new X509Certificate[0]; 
          }


        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
        {
            // TODO Auto-generated method stub

        }
        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
        {
            // TODO Auto-generated method stub

        }
      }};

      // Ignore differences between given hostname and certificate hostname
      HostnameVerifier hv = new HostnameVerifier() {

        @Override
        public boolean verify(String arg0, SSLSession arg1)
        {
            // TODO Auto-generated method stub
            return true;
        }
      };

      // Install the all-trusting trust manager
      try {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
      } catch (Exception e) {}
}
