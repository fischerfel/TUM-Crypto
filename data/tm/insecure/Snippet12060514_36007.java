//SSL Cert workaround

// always verify the host - dont check for certificate 
final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() { 
    public boolean verify(String hostname, SSLSession session) { 
        return true; 
    } 
}; 

/** 
 * Trust every server - dont check for any certificate 
 */ 
private static void trustAllHosts() { 
        // Create a trust manager that does not validate certificate chains 
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() { 
        public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
            return new java.security.cert.X509Certificate[] {}; 
        } 

        public void checkClientTrusted(X509Certificate[] chain, 
            String authType) throws CertificateException { 
        } 

        public void checkServerTrusted(X509Certificate[] chain, 
            String authType) throws CertificateException { 
        } 
        } }; 

        // Install the all-trusting trust manager 
        try { 
        SSLContext sc = SSLContext.getInstance("TLS"); 
        sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory()); 
        } catch (Exception e) { 
        e.printStackTrace(); 
        } 
}
