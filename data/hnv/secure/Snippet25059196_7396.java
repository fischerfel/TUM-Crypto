HttpParams params = new BasicHttpParams();
HttpConnectionParams.setConnectionTimeout(params, 15000); //15 seconds
HttpConnectionParams.setSoTimeout(params, 300000); // 5 minutes

HttpClient client = getHttpClient(params);
HttpPost post = new HttpPost(uri);
post.setEntity(new ByteArrayEntity(requestByteArray));
HttpResponse httpResponse = client.execute(post);

    ....

public static HttpClient getHttpClient(HttpParams params) {
    try {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);

        SSLSocketFactory sf = new TrustAllCertsSSLSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);


        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
        DefaultHttpClient client = new DefaultHttpClient(ccm, params);
        // below line of code will disable the retrying of HTTP request when connection is timed
        // out.

        client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
        return client;
    } catch (Exception e) {
        return new DefaultHttpClient();
    }
}
