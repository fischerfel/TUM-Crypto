class UnTrustworthyTrustManager
        implements X509TrustManager
{
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
    public void checkServerTrusted(X509Certificate[] arg0, String arg1)throws CertificateException {}
    public X509Certificate[] getAcceptedIssuers() { return null; }
}
