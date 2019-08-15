try {
  TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
     @Override
     public X509Certificate[] getAcceptedIssuers() {
       return null;
     }

     @Override
     public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
       // Not implemented
     }

     @Override
     public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
       // Not implemented
     }
   }};
   SSLContext sc = SSLContext.getInstance("TLS");

   sc.init(null, trustAllCerts, new java.security.SecureRandom());

   HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
 } catch (Exception e) {
   LogSaveUtil.savePayLog("disableHttpsVerify" + e.toString());
 }
