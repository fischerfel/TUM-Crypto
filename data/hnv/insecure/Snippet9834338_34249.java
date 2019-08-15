HttpParams params = new BasicHttpParams();
HttpConnectionParams.setConnectionTimeout(params, 5000);
HttpConnectionParams.setSoTimeout(params, 30000);
HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

DefaultHttpClient httpClient = new DefaultHttpClient();

SchemeRegistry registry = new SchemeRegistry();
SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
registry.register(new Scheme("https", socketFactory, 443));
SingleClientConnManager mgr = new SingleClientConnManager(httpClient.getParams(), registry);
DefaultHttpClient client = new DefaultHttpClient(mgr, httpClient.getParams());

// Set verifier      
HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
