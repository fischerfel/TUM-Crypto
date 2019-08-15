HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

DefaultHttpClient client = new DefaultHttpClient();

SchemeRegistry registry = new SchemeRegistry();
SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
registry.register(new Scheme("https", socketFactory, 443));
SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

// Set verifier     
HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

// Example send http request
 //final String url = "https://encrypted.google.com/";
 final String url = "https://ipaddress:8181/RESTTest/cars";
 HttpPost httpPost = new HttpPost(url);
  try{
     HttpResponse response = httpClient.execute(httpPost);
     System.out.println(response);
 }catch(Exception e){
     e.printStackTrace();
 }
