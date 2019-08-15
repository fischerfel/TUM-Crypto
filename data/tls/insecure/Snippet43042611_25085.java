 TrustManager[] trustAllCerts = new TrustManager[] {
    new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
    }};

 HostnameVerifier hv = new HostnameVerifier() {
     public boolean verify(String hostname, SSLSession session) { return true; }
 };

try {
  SSLContext sc = SSLContext.getInstance("SSL");
  sc.init(null, trustAllCerts, new SecureRandom());
  HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
  HttpsURLConnection.setDefaultHostnameVerifier(hv);
} catch (Exception e) {
}
