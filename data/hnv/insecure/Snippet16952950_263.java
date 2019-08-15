HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

DefaultHttpClient client = new DefaultHttpClient();
SchemeRegistry registry = new SchemeRegistry();
SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
registry.register(new Scheme("https", socketFactory, 443));
SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());                  HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
HttpPost httppost = new HttpPost("https://server.example.com/Login");
List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(
                            2);
                    nameValuePairs.add(new BasicNameValuePair("LoginId",uname));
                    nameValuePairs.add(new BasicNameValuePair("Password",pass));
try {
httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
HttpResponse response = httpClient.execute(httppost);
if (response.getStatusLine().getStatusCode() == 200) {

}               
Log.i("zacharia", "Response :"+EntityUtils.toString(response.getEntity()));
} catch (Exception e) {
}
