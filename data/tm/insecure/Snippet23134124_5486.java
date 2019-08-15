// Create and initialize HTTP parameters
HttpParams params = new BasicHttpParams();
HttpClientParams.setRedirecting(params, true );

// Set the timeout in milliseconds until a connection is established.
HttpConnectionParams.setConnectionTimeout( params, 5000 );

// Set the default socket timeout (SO_TIMEOUT)
// in milliseconds which is the timeout for waiting for data.
HttpConnectionParams.setSoTimeout( params, 10000 );

// The params are read in the ctor of the pool constructed by
// ThreadSafeClientConnManager, and need to be set before constructing it.
ConnManagerParams.setMaxTotalConnections(params, 15);
ConnPerRoute cpr = new ConnPerRoute() {
   @Override
   public int getMaxForRoute(HttpRoute httpRoute) { return 5; }
};

ConnManagerParams.setMaxConnectionsPerRoute(params, cpr);

HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

// Create and initialize scheme registry
SchemeRegistry schemeRegistry = new SchemeRegistry();
schemeRegistry.register( new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

/* Since I'm in a development environment I need to trust self-signed certs */
SSLSocketFactory sslSocketFactory = null;
try {
   X509TrustManager tm = new X509TrustManager() {
      public void checkClientTrusted(X509Certificate[] xcs, String string)
         throws CertificateException { }

      public void checkServerTrusted(X509Certificate[] xcs, String string)
         throws CertificateException { }

      public X509Certificate[] getAcceptedIssuers() { return null; }
   };

   SSLContext ctx = SSLContext.getInstance("TLS");
   ctx.init(null, new TrustManager[]{tm}, null);

   sslSocketFactory = new TrustAllSSLSocketFactory(ctx);
   if (sslSocketFactory != null)
      sslSocketFactory.setHostnameVerifier(
          SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

} catch (Exception ex) {
   Log.e(TAG, ex.getMessage(), ex);
   sslSocketFactory = null;
}

if (sslSocketFactory == null) {
   sslSocketFactory = SSLSocketFactory.getSocketFactory();
   sslSocketFactory.setHostnameVerifier(
      SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
}

schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));

// Create an HttpClient with the ThreadSafeClientConnManager.
// This connection manager must be used if more than one thread will
// be using the HttpClient.
ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

DefaultHttpClient client = new DefaultHttpClient(cm, params);

HttpProtocolParams.setUseExpectContinue(client.getParams(), false);

HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
   public boolean retryRequest(IOException exception, int executionCount,
      HttpContext context) {
      // retry a max of 5 times
      if(executionCount >= 5) { return false; }
      if(exception instanceof NoHttpResponseException){
         return true;
      } else if (exception instanceof ClientProtocolException){
         return true;
      }
      return false;
   }
};

client.setHttpRequestRetryHandler(retryHandler);

/* Cookie Management */
CookiesStore cookieStore = new BasicCookieStore();
client.setCookieStore(cookieStore);
