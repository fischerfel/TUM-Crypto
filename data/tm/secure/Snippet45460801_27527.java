//tmf.init(...);

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
           //Original trust checking
           origTrustmanager.checkServerTrusted(certs, authType);

           //Check revocation with each CRL
           //from docs: CertificateException - if the certificate chain is not trusted by this TrustManager.
           for (CRL crl: crls){
               if (crl.isRevoked(certs[0]){
                    throw new CertificateException (e);
               }
           }
       }
   }
};
