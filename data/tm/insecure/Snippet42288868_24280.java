final TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                  return new X509Certificate[0];
            }
         }
     };

// Install the all-trusting trust manager
SSLContext sslContext;
try {
     sslContext = SSLContext.getInstance("SSL");
     sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
 } catch (NoSuchAlgorithmException | KeyManagementException e) {
     e.printStackTrace();
     FirebaseCrash.report(e);
     return null;
}

// Create an ssl socket factory with our all-trusting manager
final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
