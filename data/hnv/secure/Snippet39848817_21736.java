System.setProperty("https.proxyHost", proxyHost);
System.setProperty("https.proxyPort", ""+proxyPort);
URL url = new URL (request);
SSLServerSocketFactory ssf = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
SSLServerSocket ss = (SSLServerSocket)ssf.createServerSocket();

SSLContext context = SSLContext.getInstance("SSL");
context.init(null, trustManager, new SecureRandom()); // bypass certificate

SSLSocketFactory sf = context.getSocketFactory();
HttpsURLConnection.setDefaultSSLSocketFactory(sf);

HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

connection.setSSLSocketFactory(sf);
connection.setHostnameVerifier(new TrustAllHostNameVerifier ()); // bypass certificate

connection.setRequestMethod("GET");
connection.setDoOutput(true);
InputStream content = (InputStream)connection.getInputStream();
BufferedReader in = new BufferedReader (new InputStreamReader (content));
String line;
while ((line = in.readLine()) != null) {
    System.out.println(line);
}
