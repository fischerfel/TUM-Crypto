private static class MyTrustManager implements X509TrustManager
{

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
         throws CertificateException
    {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
        throws CertificateException
    {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers()
    {
        return null;
    }

}

...

HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
try
{
    // Create an SSLContext that uses our TrustManager
    SSLContext context = SSLContext.getInstance("TLS");
    TrustManager[] tmlist = {new MyTrustManager()};
    context.init(null, tmlist, null);
    conn.setSSLSocketFactory(context.getSocketFactory());
}
catch (NoSuchAlgorithmException e)
{
    throw new IOException(e);
} catch (KeyManagementException e)
{
    throw new IOException(e);
}
conn.setRequestMethod("GET");
int rcode = conn.getResponseCode();
