TrustManagerFactory tmf = TrustManagerFactory.getInstance(
    TrustManagerFactory.getDefaultAlgorithm());
// Initialise the TMF as you normally would, for example:
tmf.init((KeyStore)null); 

TrustManager[] trustManagers = tmf.getTrustManagers();
final X509TrustManager origTrustmanager = (X509TrustManager)trustManagers[0];

TrustManager[] wrappedTrustManagers = new TrustManager[]{
   new X509TrustManager() {
       public java.security.cert.X509Certificate[] getAcceptedIssuers() {
          return origTrustmanager.getAcceptedIssuers();
       }

       public void checkClientTrusted(X509Certificate[] certs, String authType) {
           origTrustmanager.checkClientTrusted(certs, authType);
       }

       public void checkServerTrusted(X509Certificate[] certs, String authType) {
           try {
               origTrustmanager.checkServerTrusted(certs, authType);
           } catch (CertificateExpiredException e) {}
       }
   }
};

SSLContext sc = SSLContext.getInstance("TLS");
sc.init(null, wrappedTrustManagers, null);
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
