<% 
// Create a trust manager that does not validate certificate chains
TrustManager[] trustAllCerts = new TrustManager[]{
   new X509TrustManager() {
      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
         return null;
      }

      public void checkClientTrusted( java.security.cert.X509Certificate[] certs, String authType) {}

      public void checkServerTrusted( java.security.cert.X509Certificate[] certs, String authType) {}
   }
};

HostnameVerifier allHostsValid = new HostnameVerifier() {
   public boolean verify(String hostname, SSLSession session) {
      return true;
   }
};

try {
   SSLContext sc = SSLContext.getInstance("SSL");
   sc.init(null, trustAllCerts, new java.security.SecureRandom());
   HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
   HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
} catch (Exception e) {}
%>
