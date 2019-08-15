public class HTTPSClient extends DefaultHttpClient
{

    public HTTPSClient() 
    {
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager()
    {
        SchemeRegistry registry = new SchemeRegistry();

        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
        final SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        //socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", socketFactory, 80));
        registry.register(new Scheme("https", socketFactory, 443));
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");

        return new SingleClientConnManager(params, registry);
    }
}
