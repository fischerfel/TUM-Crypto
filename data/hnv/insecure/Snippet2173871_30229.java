public static final String USER_AGENT = "Mozilla/5.0 (Linux; U; Android 1.1; en-us;dream) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2";
private DefaultHttpClient getThreadSafeHttpClient() {
    final HttpParams params = new BasicHttpParams();
    params.setParameter("http.useragent", USER_AGENT);
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(params, "UTF-8");
    final SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
    sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    registry.register(new Scheme("https", sslSocketFactory, 443));
    final ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
    final DefaultHttpClient httpclient = new DefaultHttpClient(manager, params);
    // how to handle retries
    final HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
        public boolean retryRequest(final IOException exception, final int executionCount, final HttpContext context) {
            if (executionCount >= 5) {
                // Do not retry if over max retry count
                return false;
            }
            if (exception instanceof NoHttpResponseException) {
                // Retry if the server dropped connection on us
                return true;
            }
            if (exception instanceof SSLHandshakeException) {
                // Do not retry on SSL handshake exception
                return false;
            }
            final HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
            final boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
            if (idempotent) {
                // Retry if the request is considered idempotent
                return true;
            }
            return false;
        }

    };
    httpclient.setHttpRequestRetryHandler(myRetryHandler);
    return httpclient;
}
