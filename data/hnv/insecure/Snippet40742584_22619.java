HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
DefaultHttpClient httpClient = new DefaultHttpClient();
SchemeRegistry registry = new SchemeRegistry();
SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
registry.register(new Scheme("https", socketFactory, 443));
registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
SingleClientConnManager mgr = new SingleClientConnManager(httpClient.getParams(), registry);
DefaultHttpClient client = new DefaultHttpClient(mgr, httpClient.getParams());

// Set verifier
HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

DefaultHttpClient client=new DefaultHttpClient();
HttpPost request = new HttpPost(url);
request.setEntity(new StringEntity(jobject.toString()));
request.setHeader("Content-type", "application/json");
HttpResponse response = client.execute(request);
HttpEntity resEntity = response.getEntity();
String json = EntityUtils.toString(resEntity);
