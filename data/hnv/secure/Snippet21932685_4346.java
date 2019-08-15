public class MyClientHttpRequestFactory extends SimpleClientHttpRequestFactory{

    @Override
    protected void prepareConnection(HttpURLConnection connection,  String httpMethod) throws IOException {
        super.prepareConnection(connection, httpMethod);
        connection.setConnectTimeout(240 * 1000);
        connection.setReadTimeout(240 * 1000);


        if ("post".equals(httpMethod.toLowerCase())) {
            setBufferRequestBody(false);
        }else {
            setBufferRequestBody(true);
        }
    }

@Override
protected HttpURLConnection openConnection(URL url, Proxy proxy) throws IOException {
    final HttpURLConnection httpUrlConnection = super.openConnection(url, proxy);

    if (url.getProtocol().toLowerCase().equals("https")
        &&
        settings.selfSignedCert().get())
    {
        try {
            ((HttpsURLConnection)httpUrlConnection).setSSLSocketFactory(getSSLSocketFactory());
            ((HttpsURLConnection)httpUrlConnection).setHostnameVerifier(new NullHostnameVerifier());
        } catch (Exception e) {
            MyLog.e(LOG_TAG, "OpenConnection", e);
        } 
    } 

    return httpUrlConnection;
}
