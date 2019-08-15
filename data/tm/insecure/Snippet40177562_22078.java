 private class TrustAll implements X509TrustManager
{
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException
{
}

public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException
{
}

public X509Certificate[] getAcceptedIssuers()
{
    return new X509Certificate[0];
}
