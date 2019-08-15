if(httpClient==null)
{
 DefaultHttpClient client = new DefaultHttpClient();

 KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
 trustStore.load(null, null);
 SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
 sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
 SchemeRegistry registry = new SchemeRegistry();
 registry.register(new Scheme("https", sf, 8443));

 mgr = new ThreadSafeClientConnManager(client.getParams(), registry);
 int timeoutConnection = 5000;
 HttpConnectionParams.setConnectionTimeout(client.getParams(), timeoutConnection);
 // Set the default socket timeout (SO_TIMEOUT) 
 // in milliseconds which is the timeout for waiting for data.
 int timeoutSocket = 7000;
 HttpConnectionParams.setSoTimeout(client.getParams(), timeoutSocket);
 httpClient = new DefaultHttpClient(mgr, client.getParams());
}
