class AlwaysTrustHostnameVerifier implements X509TrustManager 
{
    public void checkClientTrusted( X509Certificate[] x509 , String authType ) throws CertificateException { /* nothing */ }
    public void checkServerTrusted( X509Certificate[] x509 , String authType ) throws CertificateException { /* nothing */ }
    public X509Certificate[] getAcceptedIssuers() { return null; }      
}
