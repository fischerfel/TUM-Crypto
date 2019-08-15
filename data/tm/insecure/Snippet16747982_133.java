TrustManager[] trustAllCerts = new TrustManager[] { 
    new X509TrustManager() {     
        public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
            return null;
        } 
        public void checkClientTrusted( 
            java.security.cert.X509Certificate[] certificates, String authType) {
            } 
        public void checkServerTrusted( 
            java.security.cert.X509Certificate[] certificates, String authType) {
        }
    } 
}; 

try {
    SSLContext sslContext = SSLContext.getInstance("SSL"); 
    sslContext.init(null, trustAllCerts, new java.security.SecureRandom()); 
    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
} catch (GeneralSecurityException e) {
} 

//then do the ssl conversation.
