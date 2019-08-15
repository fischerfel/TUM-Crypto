X509TrustManager trust;
// Create a trust manager that does not validate certificate chains
final TrustManager[] trustAllCerts = new TrustManager[]{
        trust = new X509TrustManager() {
             @Override
             public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

             @Override
             public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

             @Override
             public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                 return new java.security.cert.X509Certificate[]{};
             }
        }
};

// Install the all-trusting trust manager
final SSLContext sslContext = SSLContext.getInstance("SSL");
sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
