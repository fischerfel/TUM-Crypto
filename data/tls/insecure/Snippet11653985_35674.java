private static HttpClient createHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
    SSLContext sslContext = SSLContext.getInstance("SSL");
    TrustManager[] trustAllCerts = new TrustManager[] { new TrustAllTrustManager() };
    sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

    SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslContext);
    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(new Scheme("https", 443, sslSocketFactory));
    schemeRegistry.register(new Scheme("http", 80, new PlainSocketFactory()));

    HttpParams params = new BasicHttpParams();
    ClientConnectionManager cm = new org.apache.http.impl.conn.SingleClientConnManager(schemeRegistry);

    // some pages require a user agent
    AbstractHttpClient httpClient = new DefaultHttpClient(cm, params);
    HttpProtocolParams.setUserAgent(httpClient.getParams(), "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:13.0) Gecko/20100101 Firefox/13.0.1");

    httpClient.setRedirectStrategy(new RedirectStrategy());

    httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
        @Override
        public void process(HttpResponse response, HttpContext context)
                throws HttpException, IOException {
            if (response.containsHeader("Location")) {
                Header[] locations = response.getHeaders("Location");
                if (locations.length > 0)
                    context.setAttribute(LAST_REDIRECT_URL, locations[0].getValue());
            }
        }
    });

    return httpClient;
}

private String getUrlAfterRedirects(HttpContext context) {
    String lastRedirectUrl = (String) context.getAttribute(LAST_REDIRECT_URL);
    if (lastRedirectUrl != null)
        return lastRedirectUrl;
    else {
        HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        HttpHost currentHost = (HttpHost)  context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
        String currentUrl = (currentReq.getURI().isAbsolute()) ? currentReq.getURI().toString() : (currentHost.toURI() + currentReq.getURI());
        return currentUrl;
    }
}

public static final String LAST_REDIRECT_URL = "last_redirect_url";
