private static final int TIMEOUT_OPS = 3000 ;
private HttpClient mHttpClient;

public String sendToken(String token) throws IOException, AuthenticationException {
    JSONArray jsonData = new JSONArray();
    jsonData.put(token);

    final HttpPost post = prepareJSONRequest(jsonData.toString(), SEND_TOKEN_METHOD);
    HttpClient httpClient = getConnection();
    Log.i(TAG, "Sending request");
    final HttpResponse resp;
    try{
        resp = httpClient.execute(post);
    }catch(Exception e){
        e.printStackTrace();
        return null;
    }
    Log.i(TAG, "Got response");
    ...
}

private HttpPost prepareJSONRequest(String rest_data, String method) throws AssertionError {
    AbstractHttpEntity entity = null;
    try {
        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(KEY_PARAM_METHOD, method));
        params.add(new BasicNameValuePair(KEY_PARAM_INPUT_TYPE, JSON));
        params.add(new BasicNameValuePair(KEY_PARAM_RESPONSE_TYPE, JSON));
        params.add(new BasicNameValuePair("rest_data", rest_data));
        entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
        final HttpPost post = new HttpPost(mServer);
        post.addHeader(entity.getContentType());
        post.setEntity(entity);
        return post;

    } catch (final UnsupportedEncodingException e) {
        // this should never happen.
        throw new AssertionError(e);
    }
}

private HttpClient getConnection() {
    if (mHttpClient == null) {
        // registers schemes for both http and https
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpProtocolParams.setUseExpectContinue(params, false);
        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT_OPS);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT_OPS);
        ConnManagerParams.setTimeout(params, TIMEOUT_OPS);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
        sslSocketFactory.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        if (!mNoCertValidation)
            registry.register(new Scheme("https", sslSocketFactory, 443));
        else
            registry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
        ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
        mHttpClient = new DefaultHttpClient(manager, params);

    }
    return mHttpClient;
}
