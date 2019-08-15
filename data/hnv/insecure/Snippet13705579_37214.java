HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

DefaultHttpClient client = new DefaultHttpClient();

SchemeRegistry registry = new SchemeRegistry();
SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
registry.register(new Scheme("https", socketFactory, 8443));
SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

// Set verifier      
HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

// Example send http request
final String url = "https://ts.xoomworks.com:8443/XoomworksTimesheets/ping/";
HttpPost httpPost = new HttpPost(url);
HttpResponse response = httpClient.execute(httpPost);

response.getStatusLine().getStatusCode();

in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
StringBuffer sb = new StringBuffer("");
String l = "";
String nl = System.getProperty("line.separator");
while ((l = in.readLine()) !=null){
    sb.append(l + nl);
}
in.close();
data = sb.toString();
return data;
