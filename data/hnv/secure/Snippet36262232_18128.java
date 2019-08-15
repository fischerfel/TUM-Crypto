HttpParams params = new BasicHttpParams();
ClientConnectionManager connectionManager = null;
try {
KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
trustStore.load(null, null);
SchemeRegistry registry = new SchemeRegistry();
SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
sf.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);     
registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
registry.register(new Scheme("https", sf, 443));
connectionManager = new SingleClientConnManager(params, registry);
}
catch (Exception e)
 {
  Log.e(TAG, Log.getStackTraceString(e));
 }

 ConnManagerParams.setTimeout(params, mTimeOut);
 HttpConnectionParams.setSoTimeout(params, mTimeOut);
 HttpConnectionParams.setConnectionTimeout(params, mTimeOut);
 HttpConnectionParams.setTcpNoDelay(params, true);

 DefaultHttpClient client = new DefaultHttpClient(connectionManager, params);

client.getCredentialsProvider().setCredentials(new AuthScope(host,port),
new UsernamePasswordCredentials(userID,passowrd));

client.setRedirectHandler(new RedirectHandler() {
@Override
public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
}
@Override
public URI getLocationURI(HttpResponse response, HttpContext context) throws ProtocolException {
}

try {
HttpGet httpget = new HttpGet(url);
HttpResponse response = client.execute(httpget);  // issue occur here
