HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
DefaultHttpClient client = new DefaultHttpClient();
SchemeRegistry registry = new SchemeRegistry();
SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
registry.register(new Scheme("https", socketFactory, 443));
SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
httpclient = new DefaultHttpClient(mgr, client.getParams());

// Set verifier     
HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
HttpGet httpget = new HttpGet(this.requestedURL);
httpget.addHeader(new BasicScheme().authenticate(creds, httpget));

try
{
    response = httpclient.execute(httpget);
}
catch(java.lang.Throwable t) {}
