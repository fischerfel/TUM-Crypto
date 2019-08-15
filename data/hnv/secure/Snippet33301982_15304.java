    HttpParams params = new BasicHttpParams();
 ClientConnectionManager connectionManager = null;
 try {
 KeyStore trustStore = KeyStore.getInstance(KeyStore
 .getDefaultType());
 trustStore.load(null, null);
 SchemeRegistry registry = new SchemeRegistry();
 SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
  sf.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
registry.register(new Scheme("http", PlainSocketFactory
 .getSocketFactory(), 80));
registry.register(new Scheme("https", sf, 443));
connectionManager = new SingleClientConnManager(params, registry);
}
catch (Exception e) {
Log.e(TAG, Log.getStackTraceString(e));
}
DefaultHttpClient client = null;
HttpGet httpget = new HttpGet(url);
HttpResponse response = client.execute(httpget);
