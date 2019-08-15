HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
HttpClient httpclient = new DefaultHttpClient();

SchemeRegistry registry = new SchemeRegistry();
SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
registry.register(new Scheme("https", socketFactory, 443));
SingleClientConnManager mgr = new SingleClientConnManager(httpclient.getParams(), registry);
DefaultHttpClient httpClient = new DefaultHttpClient(mgr, httpclient.getParams());

// Set verifier
HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

HttpPost httppost = new HttpPost(locationCache.getUrl());
httppost.setHeader("Content-type", "application/json");

JSONObject body;
body = new JSONObject();
body.put("someKey", "someData");

HttpResponse response = httpclient.execute(httppost);
