TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
    };
try
{
   SSLContext sc = SSLContext.getInstance("SSL");
   sc.init(null, trustAllCerts, new java.security.SecureRandom());
   HostnameVerifier allHostsValid = new HostnameVerifier() {
   public boolean verify(String hostname, SSLSession session) {
       return true;
   }
};
   HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
} catch (Exception e) {
    logger.info("SOAP SSL Config failed ::: "+e);
}
